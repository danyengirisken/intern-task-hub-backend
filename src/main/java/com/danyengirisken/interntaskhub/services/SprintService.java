package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.dto.SprintRequest;
import com.danyengirisken.interntaskhub.entity.Tproject;
import com.danyengirisken.interntaskhub.entity.Tsprint;
import com.danyengirisken.interntaskhub.exception.ResourceNotFoundException;
import com.danyengirisken.interntaskhub.repository.ProjectDao;
import com.danyengirisken.interntaskhub.repository.SprintDao;
import com.danyengirisken.interntaskhub.security.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Sprint CRUD is mantigi.
 *
 * Sprint'in partner'i bagli oldugu projeden gelir: ADMIN tum sprintleri gorur,
 * diger kullanicilar yalnizca kendi partnerinin projelerine ait sprintleri
 * gorur ve yalnizca o projelere sprint ekleyebilir.
 */
@Service
@Transactional
public class SprintService {

    private final SprintDao sprintDao;
    private final ProjectDao projectDao;
    private final UserContext userContext;

    public SprintService(SprintDao sprintDao, ProjectDao projectDao, UserContext userContext) {
        this.sprintDao = sprintDao;
        this.projectDao = projectDao;
        this.userContext = userContext;
    }

    // 1. Tüm sprintleri listeleme
    @Transactional(readOnly = true)
    public List<Tsprint> findAll() {
        return userContext.isAdmin()
                ? sprintDao.findAll()
                : sprintDao.findAllByPartnerId(userContext.getCurrentPartnerId());
    }

    // 2. ID'ye göre tekil sprint getirme (Düzenleme ekranı için)
    @Transactional(readOnly = true)
    public Tsprint findById(Long id) {
        return getOrThrow(id);
    }

    // 3. Yeni sprint ekleme
    public Tsprint save(SprintRequest request) {
        requireOwnedProject(request.getProjectId());

        Tsprint sprint = new Tsprint();
        apply(sprint, request);
        return sprintDao.save(sprint);
    }

    // 4. Mevcut sprinti güncelleme
    public Tsprint update(Long id, SprintRequest request) {
        Tsprint sprint = getOrThrow(id);
        requireOwnedProject(request.getProjectId());

        apply(sprint, request);
        return sprintDao.save(sprint);
    }

    // 5. Sprint silme
    public void delete(Long id) {
        sprintDao.delete(getOrThrow(id));
    }

    private void apply(Tsprint sprint, SprintRequest request) {
        sprint.setName(request.getName());
        sprint.setDescription(request.getDescription());
        sprint.setProject_id(request.getProjectId());
        sprint.setStart_date(request.getStartDate());
        sprint.setEnd_date(request.getEndDate());
        sprint.setActive(request.getActive());
    }

    /** Kaydi getirir; baska partnerin sprintine erisimi engeller. */
    private Tsprint getOrThrow(Long id) {
        Tsprint sprint = sprintDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint bulunamadı: " + id));
        requireOwnedProject(sprint.getProject_id());
        return sprint;
    }

    /** Sprint'in bagli oldugu proje, kullanicinin partnerine ait olmali. */
    private void requireOwnedProject(Long projectId) {
        if (projectId == null) {
            throw new ResourceNotFoundException("Sprint için proje seçilmelidir.");
        }
        Tproject project = projectDao.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Proje bulunamadı: " + projectId));
        userContext.requireSamePartner(project.getPartner_id());
    }
}
