package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.Task;
import com.danyengirisken.interntaskhub.entity.dto.TaskDto;
import com.danyengirisken.interntaskhub.entity.dto.TaskRequest;
import com.danyengirisken.interntaskhub.exception.ResourceNotFoundException;
import com.danyengirisken.interntaskhub.repository.TaskDao;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Görev CRUD iş mantığı. Entity <-> DTO dönüşümü burada yapılır.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskDao taskDao;

    public TaskServiceImpl(TaskDao taskDao) {
        this.taskDao = taskDao;
    }


    @Override
    public List<TaskDto> findAll() {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDto findById(Long id) {
        return toDto(getOrThrow(id));
    }

    @Override
    public TaskDto save(TaskRequest request) {
        Task task = (request.getId() != null) ? getOrThrow(request.getId()) : new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        return toDto(taskDao.save(task));
    }

    @Override
    public void delete(Long id) {
        taskDao.delete(getOrThrow(id));
    }

    private Task getOrThrow(Long id) {
        return taskDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Görev bulunamadı: " + id));
    }

    private TaskDto toDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedDate());
    }
}
