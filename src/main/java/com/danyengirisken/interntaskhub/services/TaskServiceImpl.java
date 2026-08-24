package com.danyengirisken.interntaskhub.services;

import com.danyengirisken.interntaskhub.entity.Task;
import com.danyengirisken.interntaskhub.entity.Tproject;
import com.danyengirisken.interntaskhub.entity.Tsprint;
import com.danyengirisken.interntaskhub.entity.dto.TaskDto;
import com.danyengirisken.interntaskhub.entity.dto.TaskRequest;
import com.danyengirisken.interntaskhub.exception.ResourceNotFoundException;
import com.danyengirisken.interntaskhub.repository.ProjectDao;
import com.danyengirisken.interntaskhub.repository.SprintDao;
import com.danyengirisken.interntaskhub.repository.TaskDao;
import com.danyengirisken.interntaskhub.security.UserContext;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Görev CRUD iş mantığı. Entity <-> DTO dönüşümü burada yapılır.
 *
 * Hiyerarşi: partner -> proje -> sprint -> görev.
 * Görev bir projeye zorunlu, bir sprinte opsiyonel olarak bağlıdır; partneri
 * projesinden türetilir. Böylece ADMIN tüm görevleri, diğer kullanıcılar
 * yalnızca kendi partnerinin görevlerini görür.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskDao taskDao;
    private final ProjectDao projectDao;
    private final SprintDao sprintDao;
    private final UserContext userContext;

    public TaskServiceImpl(TaskDao taskDao,
                           ProjectDao projectDao,
                           SprintDao sprintDao,
                           UserContext userContext) {
        this.taskDao = taskDao;
        this.projectDao = projectDao;
        this.sprintDao = sprintDao;
        this.userContext = userContext;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskDto> findAll() {
        List<Task> tasks = userContext.isAdmin()
                ? taskDao.findAllByOrderByIdDesc()
                : taskDao.findByPartnerIdOrderByIdDesc(userContext.getCurrentPartnerId());

        Names names = loadNames();
        return tasks.stream().map(task -> toDto(task, names)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskDto findById(Long id) {
        return toDto(getOrThrow(id), loadNames());
    }

    @Override
    public TaskDto save(TaskRequest request) {
        // Proje, gorevin partnerini de belirledigi icin once dogrulanir.
        Tproject project = projectDao.findById(request.getProjectId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Proje bulunamadı: " + request.getProjectId()));
        userContext.requireSamePartner(project.getPartner_id());

        Long sprintId = resolveSprintId(request.getSprintId(), project.getId());

        Task task = (request.getId() != null) ? getOrThrow(request.getId()) : new Task();

        task.setProjectId(project.getId());
        task.setSprintId(sprintId);
        task.setPartnerId(project.getPartner_id());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        return toDto(taskDao.save(task), loadNames());
    }

    @Override
    public void delete(Long id) {
        taskDao.delete(getOrThrow(id));
    }

    /** Sprint verildiyse secilen projeye ait olmalidir; verilmediyse gorev backlog'dadir. */
    private Long resolveSprintId(Long sprintId, Long projectId) {
        if (sprintId == null) {
            return null;
        }
        Tsprint sprint = sprintDao.findById(sprintId)
                .orElseThrow(() -> new ResourceNotFoundException("Sprint bulunamadı: " + sprintId));
        if (!projectId.equals(sprint.getProject_id())) {
            throw new AccessDeniedException("Seçilen sprint bu projeye ait değil.");
        }
        return sprint.getId();
    }

    /** Kaydi getirir ve baska partnerin gorevine erisimi engeller. */
    private Task getOrThrow(Long id) {
        Task task = taskDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Görev bulunamadı: " + id));
        userContext.requireSamePartner(task.getPartnerId());
        return task;
    }

    /** Liste ekranlari ek istek atmasin diye proje/sprint adlari tek seferde yuklenir. */
    private Names loadNames() {
        return new Names(
                projectDao.findAll().stream()
                        .collect(Collectors.toMap(Tproject::getId, Tproject::getName, (a, b) -> a)),
                sprintDao.findAll().stream()
                        .collect(Collectors.toMap(Tsprint::getId, Tsprint::getName, (a, b) -> a)));
    }

    private record Names(Map<Long, String> projects, Map<Long, String> sprints) {
    }

    private TaskDto toDto(Task task, Names names) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedDate(),
                task.getProjectId(),
                names.projects().get(task.getProjectId()),
                task.getSprintId(),
                names.sprints().get(task.getSprintId()));
    }
}
