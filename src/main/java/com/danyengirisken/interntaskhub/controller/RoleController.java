package com.danyengirisken.interntaskhub.controller;

import com.danyengirisken.interntaskhub.entity.dto.RoleDto;
import com.danyengirisken.interntaskhub.services.RoleService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rol uclari (rol atama ekrani icin secim listesi). JWT ile korunur.
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<RoleDto>> findAll() {
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }
}
