package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.Tproject;
import com.danyengirisken.interntaskhub.entity.dto.ProjectRequest;
import com.danyengirisken.interntaskhub.exception.ResourceNotFoundException;
import com.danyengirisken.interntaskhub.repository.PartnerDao;
import com.danyengirisken.interntaskhub.repository.ProjectDao;
import com.danyengirisken.interntaskhub.security.UserContext;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectDao projectDao;
    private final PartnerDao partnerDao;
    private final UserContext userContext;

    public ProjectServiceImpl(ProjectDao projectDao,
                              PartnerDao partnerDao,
                              UserContext userContext) {
        this.projectDao = projectDao;
        this.partnerDao = partnerDao;
        this.userContext = userContext;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tproject> findAll() {
        if (userContext.isAdmin()) {
            return projectDao.findAll();
        }
        return projectDao.findByPartner_id(userContext.getCurrentPartnerId());
    }

    @Override
    @Transactional(readOnly = true)
    public Tproject findById(Long id) {
        Tproject project = projectDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proje bulunamadı: " + id));

        if (!userContext.isAdmin()
                && !userContext.getCurrentPartnerId().equals(project.getPartner_id())) {
            throw new AccessDeniedException("Bu projeyi görüntüleme veya düzenleme yetkiniz yok.");
        }

        return project;
    }

    @Override
    public Tproject save(ProjectRequest request) {
        Tproject project;

        if (request.getId() != null) {
            project = findById(request.getId());
            // ADMIN mevcut bir projeyi baska partnere tasiyabilir.
            if (userContext.isAdmin() && request.getPartnerId() != null) {
                project.setPartner_id(requirePartner(request.getPartnerId()));
            }
        } else {
            project = new Tproject();
            project.setPartner_id(resolvePartnerId(request));
        }

        project.setDescription(request.getDescription());
        project.setName(request.getName());
        project.setCode(request.getCode());
        project.setActive(request.getActive());
        project.setStart_date(request.getStartDate());
        project.setEnd_date(request.getEndDate());

        return projectDao.save(project);
    }

    @Override
    public void delete(Long id) {
        Tproject project = findById(id);
        projectDao.delete(project);
    }

    /**
     * Yeni projenin partneri: ADMIN istedigi partneri secebilir (sistemi kullanan
     * firmalara proje acabilmesi icin), diger kullanicilar icin her zaman kendi
     * partneri kullanilir ve istekten gelen deger yok sayilir.
     */
    private Long resolvePartnerId(ProjectRequest request) {
        if (userContext.isAdmin() && request.getPartnerId() != null) {
            return requirePartner(request.getPartnerId());
        }
        return userContext.getCurrentPartnerId();
    }

    private Long requirePartner(Long partnerId) {
        if (!partnerDao.existsById(partnerId)) {
            throw new ResourceNotFoundException("Partner bulunamadı: " + partnerId);
        }
        return partnerId;
    }
}
