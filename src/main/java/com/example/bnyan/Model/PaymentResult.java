package com.example.bnyan.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResult {

    private boolean success;         // true if payment request succeeded
    private String paymentId;        // Moyasar payment ID
    private String transactionUrl;   // URL to complete payment
    private String message;          // Success or error message
}
