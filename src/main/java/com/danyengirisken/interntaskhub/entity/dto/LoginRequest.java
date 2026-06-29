package com.danyengirisken.interntaskhub.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Login istegi govdesi.
 */
@Data
public class LoginRequest {

    @NotBlank(message = "Kullanici adi zorunludur")
    private String username;

    @NotBlank(message = "Sifre zorunludur")
    private String password;
}
