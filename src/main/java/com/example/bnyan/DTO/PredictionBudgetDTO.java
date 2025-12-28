package com.example.bnyan.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionBudgetDTO {

    private Double minBudget;
    private Double maxBudget;
}
