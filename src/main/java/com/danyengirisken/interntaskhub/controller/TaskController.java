package com.danyengirisken.interntaskhub.controller;

import com.danyengirisken.interntaskhub.entity.dto.TaskDto;
import com.danyengirisken.interntaskhub.entity.dto.TaskRequest;
import com.danyengirisken.interntaskhub.services.TaskService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Görev CRUD uçları (carbon controller stili: findAll/findById/save/delete).
 * Tüm uçlar JWT ile korunur; partner (tenant) filtresi ve rol kontrolü
 * servis katmanında UserContext ile uygulanır.
 */
@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<TaskDto>> findAll() {

        return new ResponseEntity<>(taskService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<TaskDto> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(taskService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<TaskDto> save(@Valid @RequestBody TaskRequest request) {
        return new ResponseEntity<>(taskService.save(request), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
