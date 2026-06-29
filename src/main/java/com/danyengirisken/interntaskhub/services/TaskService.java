package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.dto.TaskDto;
import com.danyengirisken.interntaskhub.entity.dto.TaskRequest;
import java.util.List;

/**
 * Görev modülü iş mantığı (carbon: interface + Impl konvansiyonu).
 */
public interface TaskService {

    List<TaskDto> findAll();

    TaskDto findById(Long id);

    TaskDto save(TaskRequest request);

    void delete(Long id);
}
