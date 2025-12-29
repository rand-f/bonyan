package com.example.bnyan.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

        private Integer amount; // in SAR
        private String description;
        private String callbackUrl;

        private Card source; // Card info as an object
}

