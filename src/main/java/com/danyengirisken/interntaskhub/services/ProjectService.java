package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.Tproject;
import com.danyengirisken.interntaskhub.entity.dto.TaskDto;
import com.danyengirisken.interntaskhub.entity.dto.TaskRequest;

import java.util.List;

/**
 * Görev modülü iş mantığı (carbon: interface + Impl konvansiyonu).
 */
public interface ProjectService {

    List<Tproject> findAll();

    Tproject findById(Long id);

    Tproject save(TaskRequest request);

    void delete(Long id);
}
