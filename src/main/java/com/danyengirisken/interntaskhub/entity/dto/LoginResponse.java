package com.danyengirisken.interntaskhub.entity.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Basarili login cevabi: JWT token, kullanici bilgisi ve rolun menuleri.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tokenType;
    private UserDto user;
    private List<MenuDto> menus;
}
