package com.danyengirisken.interntaskhub.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Proje olusturma/guncelleme istegi.
 * id null ise yeni kayit, dolu ise guncelleme yapilir.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    private Long id;

    /**
     * Projenin acilacagi partner. YALNIZCA ADMIN gonderebilir; diger
     * kullanicilar icin yok sayilir ve oturumdaki kullanicinin partneri atanir.
     */
    private Long partnerId;

    private String description;

    private String name;

    private String code;

    private String active;

    private LocalDate startDate;

    private LocalDate endDate;
}
