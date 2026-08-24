package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.dto.SprintRequest;
import com.danyengirisken.interntaskhub.entity.Tsprint;
import com.danyengirisken.interntaskhub.repository.SprintDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SprintService {

    @Autowired
    private SprintDao sprintDao;

    // 1. Tüm sprintleri listeleme
    public List<Tsprint> findAll() {
        return sprintDao.findAll();
    }

    // 2. ID'ye göre tekil sprint getirme (Düzenleme ekranı için)
    public Tsprint findById(Long id) {
        return sprintDao.findById(id).orElseThrow(() -> new RuntimeException("Sprint bulunamadı!"));
    }

    // 3. Yeni sprint ekleme
    public Tsprint save(SprintRequest request) {
        Tsprint sprint = new Tsprint();
        sprint.setName(request.getName());
        sprint.setDescription(request.getDescription());
        sprint.setProject_id(request.getProjectId());
        sprint.setStart_date(request.getStartDate());
        sprint.setEnd_date(request.getEndDate());
        sprint.setActive(request.getActive());

        return sprintDao.save(sprint);
    }

    // 4. Mevcut sprinti güncelleme
    public Tsprint update(Long id, SprintRequest request) {
        Tsprint sprint = sprintDao.findById(id).orElseThrow(() -> new RuntimeException("Sprint bulunamadı!"));

        sprint.setName(request.getName());
        sprint.setDescription(request.getDescription());
        sprint.setProject_id(request.getProjectId());
        sprint.setStart_date(request.getStartDate());
        sprint.setEnd_date(request.getEndDate());
        sprint.setActive(request.getActive());

        return sprintDao.save(sprint);
    }

    // 5. Sprint silme
    public void delete(Long id) {
        sprintDao.deleteById(id);
    }
}