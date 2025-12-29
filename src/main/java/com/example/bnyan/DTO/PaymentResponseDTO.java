package com.example.bnyan.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    private Integer paymentId;

    // Stripe
    private String paymentIntentId;
    private String clientSecret;

    private String status; // PENDING, PAID, FAILED, REFUNDED

    private Integer amount; // هللة
    private String currency;

    private Integer platformFee;
    private Integer specialistAmount;

    private PayerDTO payer;
    private PayeeDTO payee;

    private String gateway; // STRIPE

    private LocalDateTime createdAt;
}
