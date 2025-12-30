package com.example.bnyan.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    //@NotNull(message = "budget can not be empty")
    @Column(columnDefinition = "double")
    private Double budget;

    //@NotEmpty(message = "project description can not be empty")
    @Column(columnDefinition = "varchar(500)")
    private String description;

    //@NotNull(message = "start date is required")
    @Column
    private LocalDate startDate;

    // @Positive(message = "duration must be a positive value")
    @Column
    private Integer duration;

    @Pattern(regexp = "^(preparing|on going|completed)")
    private String status;

    // @NotNull(message = "expected end date is required")
    private LocalDate expectedEndDate;
    //@NotNull(message = "project period can not be null")
    private Integer projectPeriod; //days
}
