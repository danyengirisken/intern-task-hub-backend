package com.danyengirisken.interntaskhub.entity.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Data;

/**
 * Görev oluşturma/güncelleme isteği.
 * id null ise yeni kayıt, dolu ise güncelleme yapılır.
 */
@Data
public class TaskRequest {

    private Long id;

    @NotBlank(message = "Başlık zorunludur")
    private String title;

    private String description;

    @NotBlank(message = "Durum zorunludur")
    private String status;

    private String priority;

    private LocalDate dueDate;
}
