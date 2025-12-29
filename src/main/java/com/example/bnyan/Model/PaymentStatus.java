package com.example.bnyan.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStatus {

    private String id;
    private String status;       // e.g., "paid", "pending", "failed"
    private Integer amount;      // in halalas
    private String currency;
    private String description;
}
