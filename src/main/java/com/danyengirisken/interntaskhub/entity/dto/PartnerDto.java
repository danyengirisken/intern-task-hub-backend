package com.danyengirisken.interntaskhub.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartnerDto {
    private Long id;
    private String code;
    private String name;
    private String usingLanguage;
    private String description;
    /** '1' aktif, '0' pasif. */
    private String active;
}
