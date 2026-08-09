package com.danyengirisken.interntaskhub.controller;

import com.danyengirisken.interntaskhub.entity.Tproject;
import com.danyengirisken.interntaskhub.entity.dto.TaskDto;
import com.danyengirisken.interntaskhub.entity.dto.TaskRequest;
import com.danyengirisken.interntaskhub.services.ProjectService;
import com.danyengirisken.interntaskhub.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Görev CRUD uçları (carbon controller stili: findAll/findById/save/delete).
 * Tüm uçlar JWT ile korunur.
 */
@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasPermission('#param','ProjectController') or hasRole('INTERN') or hasRole('ADMIN')")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

}
