package com.danyengirisken.interntaskhub.controller;

import com.danyengirisken.interntaskhub.dto.SprintRequest;
import com.danyengirisken.interntaskhub.entity.Tsprint;
import com.danyengirisken.interntaskhub.services.SprintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sprints")
@CrossOrigin(origins = "http://localhost:4200")
public class SprintController {

    private final SprintService sprintService;

    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    // 1. Tüm sprintleri listeleme (GET)
    @GetMapping
    public ResponseEntity<List<Tsprint>> findAll() {
        return ResponseEntity.ok(sprintService.findAll());
    }

    // 2. ID'ye göre tekil sprint getirme (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Tsprint> findById(@PathVariable Long id) {
        return ResponseEntity.ok(sprintService.findById(id));
    }

    // 3. Yeni sprint oluşturma (POST)
    @PostMapping
    public ResponseEntity<Tsprint> save(@RequestBody SprintRequest request) {
        return ResponseEntity.ok(sprintService.save(request));
    }

    // 4. Mevcut sprinti güncelleme (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<Tsprint> updateSprint(@PathVariable Long id, @RequestBody SprintRequest request) {
        return ResponseEntity.ok(sprintService.update(id, request));
    }

    // 5. Sprint silme (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSprint(@PathVariable Long id) {
        sprintService.delete(id);
        return ResponseEntity.ok().build();
    }
}