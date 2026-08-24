package com.danyengirisken.interntaskhub.controller;

import com.danyengirisken.interntaskhub.entity.dto.MenuDto;
import com.danyengirisken.interntaskhub.services.MenuService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Menu uclari. Menuler login cevabinda da doner; bu uc, rol degistiginde
 * yeniden login olmadan menuyu tazelemek icin kullanilir.
 */
@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/my")
    public ResponseEntity<List<MenuDto>> my() {
        return new ResponseEntity<>(menuService.findForCurrentUser(), HttpStatus.OK);
    }
}
