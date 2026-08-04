package com.danyengirisken.interntaskhub.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Bir kullaniciya rol atama istegi.
 */
@Data
public class AssignRoleRequest {

    @NotNull(message = "Kullanıcı zorunludur")
    private Long userId;

    @NotNull(message = "Rol zorunludur")
    private Long roleId;
}
