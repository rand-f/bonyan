package com.example.bnyan.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectAIDTO {

    private String description;
    private String location;
    private String landSize;
    private LocalDate startDate;
    private Double budget;
}
