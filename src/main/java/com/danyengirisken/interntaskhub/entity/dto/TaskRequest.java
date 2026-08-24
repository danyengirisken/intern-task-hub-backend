package com.danyengirisken.interntaskhub.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

/**
 * Görev oluşturma/güncelleme isteği.
 * id null ise yeni kayıt, dolu ise güncelleme yapılır.
 * partner bilgisi istemciden alınmaz; projeden türetilir.
 */
@Data
public class TaskRequest {

    private Long id;

    @NotNull(message = "Proje zorunludur")
    private Long projectId;

    /** Boş bırakılırsa görev backlog'da kalır. */
    private Long sprintId;

    @NotBlank(message = "Başlık zorunludur")
    private String title;

    private String description;

    @NotBlank(message = "Durum zorunludur")
    private String status;

    private String priority;

    private LocalDate dueDate;
}
