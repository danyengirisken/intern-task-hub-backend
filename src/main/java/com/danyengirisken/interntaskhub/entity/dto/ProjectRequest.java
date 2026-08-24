package com.danyengirisken.interntaskhub.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    private Long id;

    // partnerId alanını backend'de manuel hallettiğimiz için DTO'dan tamamen sildik.

    private String description;

    private String name;

    private String code;

    private Integer active;

    private LocalDate startDate;

    private LocalDate endDate;
}