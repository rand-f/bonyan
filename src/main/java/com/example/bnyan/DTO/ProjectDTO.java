package com.example.bnyan.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    @Positive(message = "budget must be a positive value")
    @NotNull(message = "budget can not be empty")
    @Column(columnDefinition = "double not null")
    private Double budget;

    @NotEmpty(message = "project description can not be empty")
    @Column(columnDefinition = "varchar(500) not null")
    private String description;

    @NotNull(message = "start date is required")
    private LocalDate startDate;

    @NotNull(message = "project period can not be null")
    private Integer projectPeriod;
}
