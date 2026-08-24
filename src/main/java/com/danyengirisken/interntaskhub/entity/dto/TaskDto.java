package com.danyengirisken.interntaskhub.entity.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Görev cevabı (entity doğrudan dışarı açılmaz).
 * Proje ve sprint adları, liste/pano ekranlarının ek istek atmaması için
 * doğrudan cevaba konur.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDate dueDate;
    private LocalDateTime createdDate;
    private Long projectId;
    private String projectName;
    /** null ise görev backlog'dadır. */
    private Long sprintId;
    private String sprintName;
}
