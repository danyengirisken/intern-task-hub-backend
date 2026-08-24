package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.Partner;
import com.danyengirisken.interntaskhub.entity.Role;
import com.danyengirisken.interntaskhub.entity.User;
import com.danyengirisken.interntaskhub.entity.dto.LoginRequest;
import com.danyengirisken.interntaskhub.entity.dto.LoginResponse;
import com.danyengirisken.interntaskhub.entity.dto.RegisterRequest;
import com.danyengirisken.interntaskhub.entity.dto.UserDto;
import com.danyengirisken.interntaskhub.exception.ResourceNotFoundException;
import com.danyengirisken.interntaskhub.repository.PartnerDao;
import com.danyengirisken.interntaskhub.repository.RoleDao;
import com.danyengirisken.interntaskhub.repository.UserDao;
import com.danyengirisken.interntaskhub.security.JwtService;
import com.danyengirisken.interntaskhub.security.Roles;
import java.util.ArrayList;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    /** Yeni kaydolan kullanicilarin varsayilan rolu (ADMIN degil!). */
    private static final String DEFAULT_ROLE = Roles.CUSTOMER;

    /** Yeni kaydolan kullanicilarin baglanacagi varsayilan partner kodu. */
    private static final String DEFAULT_PARTNER_CODE = "ABT";

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PartnerDao partnerDao;
    private final MenuService menuService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthServiceImpl(UserDao userDao,
                           RoleDao roleDao,
                           PartnerDao partnerDao,
                           MenuService menuService,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.partnerDao = partnerDao;
        this.menuService = menuService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userDao.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Kullanıcı bulunamadı: " + request.getUsername()));

        String roleName = user.getRole() != null ? user.getRole().getName() : DEFAULT_ROLE;

        String token = jwtService.generateToken(
                user.getId(), user.getUsername(), user.getFullName(), roleName);

        String partnerName = partnerDao.findById(user.getPartnerId())
                .map(Partner::getName)
                .orElse(null);

        UserDto userDto = new UserDto(
                user.getId(),
                user.getFullName(),
                user.getUsername(),
                roleName,
                user.getPartnerId(),
                partnerName);

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setTokenType("Bearer");
        response.setUser(userDto);
        // Menuler rolun yetkilerine gore uretilir (S_MENU -> S_PERMISSION -> S_ROLE_PERMISSION)
        response.setMenus(user.getRole() != null
                ? menuService.findByRoleId(user.getRole().getId())
                : new ArrayList<>());

        return response;
    }

    @Override
    public void register(RegisterRequest request) {
        Role role = roleDao.findByName(DEFAULT_ROLE)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Varsayılan rol bulunamadı: " + DEFAULT_ROLE));

        Partner partner = partnerDao.findByCode(DEFAULT_PARTNER_CODE)
                .or(() -> partnerDao.findAllByOrderByNameAsc().stream().findFirst())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Varsayılan partner bulunamadı: " + DEFAULT_PARTNER_CODE));

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setFullName(request.getUsername());
        newUser.setRole(role);
        newUser.setPartnerId(partner.getId());

        userDao.save(newUser);
    }
}
