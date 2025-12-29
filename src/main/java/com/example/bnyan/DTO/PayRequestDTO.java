package com.example.bnyan.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayRequestDTO {

    private Integer payerId;
    private Integer specialistId;
    private Integer amount;
    private Integer platformFee;
}
