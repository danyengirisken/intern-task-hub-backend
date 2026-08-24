package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.Role;
import com.danyengirisken.interntaskhub.entity.User;
import com.danyengirisken.interntaskhub.entity.dto.AssignRoleRequest;
import com.danyengirisken.interntaskhub.entity.dto.UserListDto;
import com.danyengirisken.interntaskhub.entity.dto.UserRequest;
import com.danyengirisken.interntaskhub.exception.ResourceNotFoundException;
import com.danyengirisken.interntaskhub.repository.PartnerDao;
import com.danyengirisken.interntaskhub.repository.RoleDao;
import com.danyengirisken.interntaskhub.repository.UserDao;
import com.danyengirisken.interntaskhub.security.Roles;
import com.danyengirisken.interntaskhub.security.UserContext;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Kullanici olusturma, guncelleme, silme ve rol atama is mantigi.
 *
 * Yetki kurallari:
 *  - ADMIN          : tum partnerlerin kullanicilarini gorur/yonetir, istedigi
 *                     partnere kullanici acar, her rolu atayabilir.
 *  - CUSTOMER_ADMIN : yalnizca KENDI partnerindeki kullanicilari gorur/yonetir;
 *                     actigi kullanicilar da kendi partnerine baglanir,
 *                     ADMIN rolunu atayamaz.
 *  - CUSTOMER       : bu uclara erisemez (403).
 *
 * Ornek akis: ADMIN "Tekonsa" partnerini ve o partnerin ilk CUSTOMER_ADMIN
 * kullanicisini acar; sonrasinda Tekonsa kendi kullanicilarini bu ekrandan
 * kendisi tanimlar.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PartnerDao partnerDao;
    private final PasswordEncoder passwordEncoder;
    private final UserContext userContext;

    public UserServiceImpl(UserDao userDao,
                           RoleDao roleDao,
                           PartnerDao partnerDao,
                           PasswordEncoder passwordEncoder,
                           UserContext userContext) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.partnerDao = partnerDao;
        this.passwordEncoder = passwordEncoder;
        this.userContext = userContext;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserListDto> findAll() {
        userContext.requireUserManager();

        List<User> users = userContext.isAdmin()
                ? userDao.findAll()
                : userDao.findByPartnerId(userContext.getCurrentPartnerId());

        Map<Long, String> partnerNames = partnerNames();

        return users.stream()
                .sorted(Comparator.comparing(User::getId))
                .map(user -> toDto(user, partnerNames))
                .toList();
    }

    @Override
    public UserListDto save(UserRequest request) {
        userContext.requireUserManager();

        boolean isNew = request.getId() == null;
        User user;

        if (isNew) {
            user = new User();
        } else {
            user = userDao.findById(request.getId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Kullanıcı bulunamadı: " + request.getId()));
            // Partner yoneticisi yalnizca kendi partnerindeki kullaniciya dokunabilir.
            userContext.requireSamePartner(user.getPartnerId());
        }

        // Kullanici adi benzersiz olmali
        String username = request.getUsername().trim();
        userDao.findByUsername(username).ifPresent(existing -> {
            if (!existing.getId().equals(request.getId())) {
                throw new DataIntegrityViolationException(
                        "Bu kullanıcı adı zaten kullanılıyor: " + username);
            }
        });

        user.setFullName(request.getFullName().trim());
        user.setUsername(username);
        user.setPartnerId(resolvePartnerId(request, user, isNew));
        user.setRole(resolveRole(request.getRoleId()));

        // Sifre: yeni kayitta zorunlu, guncellemede bos birakilirsa degismez.
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        } else if (isNew) {
            throw new DataIntegrityViolationException("Yeni kullanıcı için şifre zorunludur.");
        }

        return toDto(userDao.save(user), partnerNames());
    }

    @Override
    public void delete(Long id) {
        userContext.requireUserManager();

        User user = userDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + id));
        userContext.requireSamePartner(user.getPartnerId());

        if (user.getId().equals(userContext.getCurrentUserId())) {
            throw new AccessDeniedException("Kendi kullanıcınızı silemezsiniz.");
        }
        if (!userContext.isAdmin() && Roles.ADMIN.equals(user.getRole().getName())) {
            throw new AccessDeniedException("Sistem yöneticisi kullanıcısını silemezsiniz.");
        }

        userDao.delete(user);
    }

    @Override
    public UserListDto assignRole(AssignRoleRequest request) {
        userContext.requireUserManager();

        User user = userDao.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Kullanıcı bulunamadı: " + request.getUserId()));

        // Partner yoneticisi yalnizca kendi partnerindeki kullaniciya dokunabilir.
        userContext.requireSamePartner(user.getPartnerId());

        user.setRole(resolveRole(request.getRoleId()));
        return toDto(userDao.save(user), partnerNames());
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userDao.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanıcı bulunamadı: " + username));
    }

    /**
     * Kullanicinin partneri: ADMIN istedigi partneri secebilir, diger yoneticiler
     * icin her zaman kendi partneri kullanilir (istekten gelen deger yok sayilir).
     */
    private Long resolvePartnerId(UserRequest request, User user, boolean isNew) {
        if (!userContext.isAdmin()) {
            return userContext.getCurrentPartnerId();
        }
        if (request.getPartnerId() == null) {
            // ADMIN partner secmediyse: yeni kayitta kendi partneri, guncellemede mevcut kalir.
            return isNew ? userContext.getCurrentPartnerId() : user.getPartnerId();
        }
        if (!partnerDao.existsById(request.getPartnerId())) {
            throw new ResourceNotFoundException("Partner bulunamadı: " + request.getPartnerId());
        }
        return request.getPartnerId();
    }

    /** Sistem yoneticisi rolunu yalnizca sistem yoneticisi verebilir. */
    private Role resolveRole(Long roleId) {
        Role role = roleDao.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Rol bulunamadı: " + roleId));

        if (!userContext.isAdmin() && Roles.ADMIN.equals(role.getName())) {
            throw new AccessDeniedException("Sistem yöneticisi rolünü atama yetkiniz yok.");
        }
        return role;
    }

    private Map<Long, String> partnerNames() {
        return partnerDao.findAll().stream()
                .collect(Collectors.toMap(p -> p.getId(), p -> p.getName(), (a, b) -> a));
    }

    private UserListDto toDto(User user, Map<Long, String> partnerNames) {
        return new UserListDto(
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                user.getRole().getId(),
                user.getRole().getName(),
                user.getPartnerId(),
                partnerNames.get(user.getPartnerId()));
    }
}
