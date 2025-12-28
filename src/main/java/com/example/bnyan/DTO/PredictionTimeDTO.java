package com.example.bnyan.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PredictionTimeDTO {
    private Integer expectedProjectPeriod;
    private String expectedProjectEndDte;
}
