package com.danyengirisken.interntaskhub.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login cevabinda donen, disariya guvenli kullanici bilgisi (sifre icermez).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String fullName;
    private String username;
    private String role;
}
