package com.danyengirisken.interntaskhub.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Kullanici olusturma/guncelleme istegi.
 * id null ise yeni kayit, dolu ise guncelleme yapilir.
 */
@Data
public class UserRequest {

    private Long id;

    @NotBlank(message = "Ad Soyad zorunludur")
    @Size(max = 150, message = "Ad Soyad en fazla 150 karakter olabilir")
    private String fullName;

    @NotBlank(message = "Kullanıcı adı zorunludur")
    @Size(max = 100, message = "Kullanıcı adı en fazla 100 karakter olabilir")
    private String username;

    /** Yeni kayitta zorunlu; guncellemede bos birakilirsa sifre degismez. */
    @Size(min = 4, max = 100, message = "Şifre en az 4 karakter olmalıdır")
    private String password;

    @NotNull(message = "Rol zorunludur")
    private Long roleId;

    /**
     * Yalnizca ADMIN gonderebilir (kullaniciyi istedigi partnere acar).
     * Partner yoneticisi icin yok sayilir; kendi partneri kullanilir.
     */
    private Long partnerId;
}
