package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.Tproject;
import com.danyengirisken.interntaskhub.entity.dto.ProjectRequest;
import com.danyengirisken.interntaskhub.exception.ResourceNotFoundException;
import com.danyengirisken.interntaskhub.repository.ProjectDao;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

    private final ProjectDao projectDao;
    private final UserService userService;

    public ProjectServiceImpl(ProjectDao projectDao, UserService userService) {
        this.projectDao = projectDao;
        this.userService = userService;
    }

    private boolean isAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN") || a.getAuthority().equals("ADMIN"));
    }

    private Long getCurrentPartnerId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        return userService.findByUsername(username).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Tproject> findAll() {
        if (isAdmin()) {
            return projectDao.findAll();
        } else {
            return projectDao.findByPartner_id(getCurrentPartnerId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Tproject findById(Long id) {
        Tproject project = projectDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proje bulunamadı: " + id));

        if (!isAdmin() && !project.getPartner_id().equals(getCurrentPartnerId())) {
            throw new AccessDeniedException("Bu projeyi görüntüleme veya düzenleme yetkiniz yok.");
        }

        return project;
    }

    @Override
    public Tproject save(ProjectRequest request) {
        Tproject project;

        if (request.getId() != null) {
            project = findById(request.getId());
        } else {
            project = new Tproject();
            project.setPartner_id(getCurrentPartnerId());
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
}