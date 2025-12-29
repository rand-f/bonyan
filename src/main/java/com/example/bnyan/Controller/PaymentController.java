package com.example.bnyan.Controller;

import com.example.bnyan.Model.PaymentRequest;
import com.example.bnyan.Model.PaymentResult;
import com.example.bnyan.Model.PaymentStatus;
import com.example.bnyan.Service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResult> create(@RequestBody PaymentRequest request) {
        PaymentResult result = paymentService.createPayment(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<PaymentStatus> status(@PathVariable String id) {
        PaymentStatus status = paymentService.getPaymentStatus(id);
        return ResponseEntity.ok(status);
    }

}

