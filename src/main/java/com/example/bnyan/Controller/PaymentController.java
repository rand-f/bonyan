package com.example.bnyan.Controller;

import com.example.bnyan.Model.PaymentRequest;
import com.example.bnyan.Service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/card")
    public ResponseEntity<String> processPayment(@RequestBody @Valid PaymentRequest paymentRequest) {
        return paymentService.processPayment(paymentRequest);
    }


}
