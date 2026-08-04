package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.Menu;
import com.danyengirisken.interntaskhub.entity.Permission;
import com.danyengirisken.interntaskhub.entity.User;
import com.danyengirisken.interntaskhub.entity.dto.LoginRequest;
import com.danyengirisken.interntaskhub.entity.dto.LoginResponse;
import com.danyengirisken.interntaskhub.entity.dto.MenuDto;
import com.danyengirisken.interntaskhub.entity.dto.UserDto;
import com.danyengirisken.interntaskhub.repository.MenuDao;
import com.danyengirisken.interntaskhub.repository.UserDao;
import com.danyengirisken.interntaskhub.security.JwtService;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Kullaniciyi dogrular, JWT uretir ve rolun menuleriyle birlikte cevabi hazirlar.
 */
@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final String TOKEN_TYPE = "Bearer";

    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final MenuDao menuDao;
    private final JwtService jwtService;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserDao userDao,
                           MenuDao menuDao,
                           JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
        this.menuDao = menuDao;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        // Hatali kimlik bilgisinde BadCredentialsException firlatir -> 401 doner.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));

        User user = userDao.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Kullanici bulunamadi: " + request.getUsername()));

        String role = user.getRole().getName();
        String token = jwtService.generateToken(
                user.getId(), user.getUsername(), user.getFullName(), role);

        UserDto userDto = new UserDto(
                user.getId(), user.getFullName(), user.getUsername(), role);

        List<MenuDto> menus = buildMenusForRole(user);

        return new LoginResponse(token, TOKEN_TYPE, userDto, menus);
    }

    /**
     * Kullanicinin rolunun yetkilerine gore gorunur menuleri kurar.
     * Bir menu; aktifse ve (permissionId'si null ise ya da rolun yetkilerinden
     * birine esitse) gorunur. Cocuk menusu kalmayan grup (page'i null) menuleri
     * elenir (bos grup gosterilmez).
     */
    private List<MenuDto> buildMenusForRole(User user) {
        Set<Long> permissionIds = user.getRole().getPermissions().stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());

        // Aktif + yetkisi olan menuler
        List<Menu> allowed = menuDao.findAll().stream()
                .filter(menu -> !Boolean.FALSE.equals(menu.getActive()))
                .filter(menu -> menu.getPermissionId() == null
                        || permissionIds.contains(menu.getPermissionId()))
                .toList();

        // En az bir gorunur cocugu olan ust menu id'leri
        Set<Long> parentsWithChildren = allowed.stream()
                .map(Menu::getParentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return allowed.stream()
                // Grup (page null) sadece gorunur cocugu varsa kalir; ekranlar her zaman kalir
                .filter(menu -> menu.getPage() != null || parentsWithChildren.contains(menu.getId()))
                .map(menu -> new MenuDto(
                        menu.getId(), menu.getParentId(), menu.getTitle(),
                        menu.getPage(), menu.getIcon(), menu.getMenuOrder()))
                .sorted(Comparator.comparing(
                        MenuDto::getMenuOrder,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }
}
