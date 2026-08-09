package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.Task;
import com.danyengirisken.interntaskhub.entity.Tproject;
import com.danyengirisken.interntaskhub.entity.dto.TaskDto;
import com.danyengirisken.interntaskhub.entity.dto.TaskRequest;
import com.danyengirisken.interntaskhub.exception.ResourceNotFoundException;
import com.danyengirisken.interntaskhub.repository.ProjectDao;
import com.danyengirisken.interntaskhub.repository.TaskDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Görev CRUD iş mantığı. Entity <-> DTO dönüşümü burada yapılır.
 */
@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectDao projectDao;

    public ProjectServiceImpl(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }


    @Override
    public List<Tproject> findAll() {
        return List.of();
    }

    @Override
    public Tproject findById(Long id) {
        return null;
    }

    @Override
    public Tproject save(TaskRequest request) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
