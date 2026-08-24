package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.Tproject;
import com.danyengirisken.interntaskhub.entity.dto.ProjectRequest;

import java.util.List;

public interface ProjectService {

    List<Tproject> findAll();

    Tproject findById(Long id);

    Tproject save(ProjectRequest request);

    void delete(Long id);
}