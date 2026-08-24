package com.danyengirisken.interntaskhub.controller;

import com.danyengirisken.interntaskhub.entity.dto.LoginRequest;
import com.danyengirisken.interntaskhub.entity.dto.LoginResponse;
import com.danyengirisken.interntaskhub.entity.dto.RegisterRequest;
import com.danyengirisken.interntaskhub.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // YENİ EKLENEN LOGİN METODU
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Giriş işlemi sırasında hata: " + e.getMessage());
            // Frontend'in yakalayabilmesi için JSON formatında veya basit mesaj olarak hata dönüyoruz
            return ResponseEntity.badRequest().body("Kullanıcı adı veya şifre hatalı!");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.ok("Kayıt işlemi başarıyla gerçekleşti.");
        } catch (Exception e) {
            System.err.println("Kayıt işlemi sırasında hata: " + e.getMessage());
            return ResponseEntity.badRequest().body("Kayıt başarısız. Lütfen bilgilerinizi kontrol edin veya farklı bir kullanıcı adı deneyin.");
        }
    }
}