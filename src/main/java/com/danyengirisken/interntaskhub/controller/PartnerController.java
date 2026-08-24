package com.danyengirisken.interntaskhub.controller;

import com.danyengirisken.interntaskhub.entity.dto.PartnerDto;
import com.danyengirisken.interntaskhub.entity.dto.PartnerRequest;
import com.danyengirisken.interntaskhub.services.PartnerService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/partners")
@CrossOrigin(origins = "http://localhost:4200")
public class PartnerController {

    private final PartnerService partnerService;

    public PartnerController(PartnerService partnerService) {
        this.partnerService = partnerService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<PartnerDto>> findAll() {
        return new ResponseEntity<>(partnerService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<PartnerDto> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(partnerService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<PartnerDto> save(@Valid @RequestBody PartnerRequest request) {
        return new ResponseEntity<>(partnerService.save(request), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        partnerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
