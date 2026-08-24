package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.Role;
import com.danyengirisken.interntaskhub.entity.User;
import com.danyengirisken.interntaskhub.entity.dto.LoginRequest;
import com.danyengirisken.interntaskhub.entity.dto.LoginResponse;
import com.danyengirisken.interntaskhub.entity.dto.RegisterRequest;
import com.danyengirisken.interntaskhub.repository.UserDao;
import com.danyengirisken.interntaskhub.security.JwtService; // JwtService Importu
import org.springframework.security.authentication.AuthenticationManager; // AuthManager Importu
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // CONSTRUCTOR GÜNCELLENDİ: Yeni servisleri içeriye alıyoruz
    public AuthServiceImpl(UserDao userDao,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtService jwtService) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        // 1. Spring Security ile kullanıcı adı ve şifreyi doğrula
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. Doğrulama başarılıysa, token üretmek için kullanıcıyı veritabanından çek
        User user = userDao.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        // 3. Kullanıcının rolünü metin formatında al
        // (Eğer Role class'ının içinde spesifik bir isim alanı varsa örn: getName() onu kullanabilirsin)
        String roleName = user.getRole() != null ? user.getRole().toString() : "USER";

        // 4. JwtService'i kullanarak Token'ı üret
        String token = jwtService.generateToken(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                roleName
        );

        // 5. Yanıtı hazırla ve Angular'a gönder
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setTokenType("Bearer"); // JWT standardı gereği token tipini belirtiyoruz

        // Eksik olan UserDto nesnesini oluşturup içini dolduruyoruz
        // (Eğer import edilmediyse en yukarıya ekle: import com.danyengirisken.interntaskhub.entity.dto.UserDto;)
        com.danyengirisken.interntaskhub.entity.dto.UserDto userDto = new com.danyengirisken.interntaskhub.entity.dto.UserDto();
        userDto.setUsername(user.getUsername());

        // Eğer UserDto içinde aşağıdaki alanlar (id, fullName) tanımlıysa onları da set edebilirsin.
        // Tanımlı değilse sadece setUsername kalması yeterlidir, Angular'ın çökmesini engeller:
        // userDto.setId(user.getId());
        // userDto.setFullName(user.getFullName());

        response.setUser(userDto);

        // Menü listesi şimdilik boş gidebilir, eğer Angular menüleri bekliyorsa boş bir liste dönmek hatayı önler
        response.setMenus(new java.util.ArrayList<>());

        return response;
    }

    @Override
    public void register(RegisterRequest request) {
        // ... Burası bir önceki adımda yazdığımız haliyle birebir aynı kalacak ...
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setFullName(request.getUsername());

        Role defaultRole = new Role();
        defaultRole.setId(1L);
        newUser.setRole(defaultRole);

        userDao.save(newUser);
    }
}