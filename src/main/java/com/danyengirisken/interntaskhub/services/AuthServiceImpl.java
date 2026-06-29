package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.User;
import com.danyengirisken.interntaskhub.entity.dto.LoginRequest;
import com.danyengirisken.interntaskhub.entity.dto.LoginResponse;
import com.danyengirisken.interntaskhub.entity.dto.MenuDto;
import com.danyengirisken.interntaskhub.entity.dto.UserDto;
import com.danyengirisken.interntaskhub.repository.UserDao;
import com.danyengirisken.interntaskhub.security.JwtService;
import java.util.Comparator;
import java.util.List;
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
    private final JwtService jwtService;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserDao userDao,
                           JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
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

        List<MenuDto> menus = user.getRole().getMenus().stream()
                .filter(menu -> !Boolean.FALSE.equals(menu.getActive()))
                .map(menu -> new MenuDto(
                        menu.getId(), menu.getParentId(), menu.getTitle(),
                        menu.getPage(), menu.getIcon(), menu.getMenuOrder()))
                .sorted(Comparator.comparing(
                        MenuDto::getMenuOrder,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();

        return new LoginResponse(token, TOKEN_TYPE, userDto, menus);
    }
}
