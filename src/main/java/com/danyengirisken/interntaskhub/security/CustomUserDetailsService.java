package com.danyengirisken.interntaskhub.security;

import com.danyengirisken.interntaskhub.entity.User;
import com.danyengirisken.interntaskhub.repository.UserDao;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security'nin kullaniciyi veritabanindan yuklemesini saglar.
 * Rol, {@code ROLE_} on ekiyle yetki olarak eklenir.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDao userDao;

    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Kullanici bulunamadi: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(List.of(
                        new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())))
                .build();
    }
}
