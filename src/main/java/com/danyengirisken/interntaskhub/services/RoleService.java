package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.dto.RoleDto;
import java.util.List;

/**
 * Rol modulu is mantigi (carbon: interface + Impl konvansiyonu).
 */
public interface RoleService {

    List<RoleDto> findAll();
}
