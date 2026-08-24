package com.danyengirisken.interntaskhub.controller;

import com.danyengirisken.interntaskhub.entity.Tproject;
import com.danyengirisken.interntaskhub.entity.dto.ProjectRequest;
import com.danyengirisken.interntaskhub.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:4200")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Tproject>> findAll() {
        return ResponseEntity.ok(projectService.findAll());
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Tproject> findById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<Tproject> save(
            @Valid @RequestBody ProjectRequest request) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(projectService.save(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}