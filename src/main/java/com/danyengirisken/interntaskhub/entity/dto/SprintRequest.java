package com.danyengirisken.interntaskhub.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class SprintRequest {
    private String name;
    private String description;
    private Long projectId; // Sprint'in hangi projeye ait olduğunu belirler
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer active;
}