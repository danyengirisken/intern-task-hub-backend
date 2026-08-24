package com.danyengirisken.interntaskhub.security;

import com.danyengirisken.interntaskhub.entity.User;
import com.danyengirisken.interntaskhub.repository.UserDao;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class UserContext {

    private final UserDao userDao;

    public UserContext(UserDao userDao) {
        this.userDao = userDao;
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Oturum bulunamadı.");
        }
        return auth.getName();
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        String username = getCurrentUsername();
        return userDao.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("Kullanıcı bulunamadı: " + username));
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }

    public Long getCurrentPartnerId() {
        Long partnerId = getCurrentUser().getPartnerId();
        if (partnerId == null) {
            throw new AccessDeniedException(
                    "Kullanıcının bağlı olduğu bir partner yok. Lütfen sistem yöneticisiyle görüşün.");
        }
        return partnerId;
    }

    /** Oturumdaki kullanicinin rol adi (orn. ADMIN). */
    public String getCurrentRoleName() {
        User user = getCurrentUser();
        return user.getRole() != null ? user.getRole().getName() : Roles.CUSTOMER;
    }

    /** Sistem yoneticisi mi? (tum partnerleri gorur) */
    public boolean isAdmin() {
        return hasRole(Roles.ADMIN);
    }

    /** Partner (musteri) yoneticisi mi? (yalnizca kendi partneri) */
    public boolean isCustomerAdmin() {
        return hasRole(Roles.CUSTOMER_ADMIN);
    }

    /** Kullanici listeleme / rol atama yetkisi var mi? */
    public boolean canManageUsers() {
        return isAdmin() || isCustomerAdmin();
    }

    /** ADMIN degilse 403 firlatir (orn. Partner ekrani). */
    public void requireAdmin() {
        if (!isAdmin()) {
            throw new AccessDeniedException("Bu işlem için sistem yöneticisi yetkisi gerekiyor.");
        }
    }

    /** Kullanici yonetimi yetkisi yoksa 403 firlatir. */
    public void requireUserManager() {
        if (!canManageUsers()) {
            throw new AccessDeniedException("Bu işlem için kullanıcı yönetimi yetkisi gerekiyor.");
        }
    }

    /**
     * ADMIN degilse, verilen partner oturumdaki kullanicinin partneri olmak
     * zorundadir. Baska bir partnerin kaydina erisimde 403 firlatir.
     */
    public void requireSamePartner(Long partnerId) {
        if (isAdmin()) {
            return;
        }
        if (partnerId == null || !partnerId.equals(getCurrentPartnerId())) {
            throw new AccessDeniedException("Bu kayıt sizin partnerinize ait değil.");
        }
    }

    private boolean hasRole(String roleName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(a -> ("ROLE_" + roleName).equals(a.getAuthority())
                        || roleName.equals(a.getAuthority()));
    }
}
