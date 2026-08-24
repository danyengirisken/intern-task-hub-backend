package com.danyengirisken.interntaskhub.controller;

import com.danyengirisken.interntaskhub.entity.dto.AssignRoleRequest;
import com.danyengirisken.interntaskhub.entity.dto.UserListDto;
import com.danyengirisken.interntaskhub.entity.dto.UserRequest;
import com.danyengirisken.interntaskhub.services.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Kullanici uclari: listeleme, olusturma/guncelleme, silme ve rol atama.
 * Tum uclar JWT ile korunur; yetki kontrolu servis katmaninda UserContext ile
 * yapilir (ADMIN tum partnerler, CUSTOMER_ADMIN yalnizca kendi partneri).
 */
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<UserListDto>> findAll() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<UserListDto> save(@Valid @RequestBody UserRequest request) {
        return new ResponseEntity<>(userService.save(request), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/assignRole")
    public ResponseEntity<UserListDto> assignRole(@Valid @RequestBody AssignRoleRequest request) {
        return new ResponseEntity<>(userService.assignRole(request), HttpStatus.OK);
    }
}
