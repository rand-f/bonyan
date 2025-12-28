package com.example.bnyan.Model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

        @NotEmpty(message = "Card holder name is required")
        private String name;

        @NotEmpty
        @Pattern(regexp = "\\d{16}", message = "Card number must be 16 digits")
        private String number;

        @NotEmpty
        @Pattern(regexp = "\\d{3}", message = "CVC must be 3 digits")
        private String cvc;

        @NotEmpty
        @Pattern(regexp = "^(0[1-9]|1[0-2])$", message = "Month must be 01–12")
        private String month;

        @NotEmpty
        @Pattern(regexp = "^20\\d{2}$", message = "Year must be valid (e.g. 2026)")
        private String year;

        @Positive(message = "Amount must be greater than zero")
        private double amount;

        @NotEmpty
        @Pattern(regexp = "SAR", message = "Currency must be SAR")
        private String currency;

        private String description;

        private String callbackUrl;
}
