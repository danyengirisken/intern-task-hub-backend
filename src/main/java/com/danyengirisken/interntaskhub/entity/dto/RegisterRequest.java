package com.danyengirisken.interntaskhub.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String username;
    private String password;

    // Eğer kayıt olurken isim, soyisim veya e-posta gibi bilgiler de
    // almak istersen onları da buraya ekleyebilirsin.
    // private String email;
    // private String firstName;
    // private String lastName;
}