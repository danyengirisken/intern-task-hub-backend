package com.danyengirisken.interntaskhub.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Kullanici listesi / rol atama ekrani icin kullanici satiri.
 * Mevcut rolu (roleId + roleName) ile birlikte doner.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListDto {
    private Long id;
    private String fullName;
    private String username;
    private Long roleId;
    private String roleName;
}
