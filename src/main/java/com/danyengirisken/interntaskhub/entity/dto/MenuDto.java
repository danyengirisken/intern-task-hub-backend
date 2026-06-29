package com.danyengirisken.interntaskhub.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Frontend navigasyonunda gosterilecek hiyerarsik menu ogesi.
 * parentId null ise en ust seviye, dolu ise alt menudur (dropdown).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {
    private Long id;
    private Long parentId;
    private String title;
    private String page;
    private String icon;
    private Integer menuOrder;
}
