package com.danyengirisken.interntaskhub.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PartnerRequest {

    private Long id;

    @NotBlank(message = "Kod zorunludur")
    @Size(max = 12, message = "Kod en fazla 12 karakter olabilir")
    private String code;

    @NotBlank(message = "Ad zorunludur")
    @Size(max = 100, message = "Ad en fazla 100 karakter olabilir")
    private String name;

    @Size(max = 10, message = "Dil kodu en fazla 10 karakter olabilir")
    private String usingLanguage;

    @Size(max = 4000, message = "Açıklama en fazla 4000 karakter olabilir")
    private String description;

    /** '1' aktif, '0' pasif. Bos gelirse '1' kabul edilir. */
    private String active;
}
