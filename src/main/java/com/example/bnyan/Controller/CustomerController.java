package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.DTO.CustomerDTO;
import com.example.bnyan.DTO.QuestionDTO;
import com.example.bnyan.Model.User;
import com.example.bnyan.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(customerService.get());
    }


    @PostMapping("/register-customer")
    public ResponseEntity<?> registerCustomer(@RequestBody @Valid CustomerDTO customerDTO) {
        customerService.registerCustomer(customerDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Customer registered successfully"));
    }

    @GetMapping("/get-by-id")
    public ResponseEntity<?> getCustomerById(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(customerService.getCustomerById(user.getId()));
    }

    @PostMapping("/ask-ai")
    public ResponseEntity<?> askAI(@RequestBody QuestionDTO questionDTO,
                                   @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(customerService.askAI(user.getId(), questionDTO.getQuestion()));
    }

    @GetMapping("/get-properties")
    public ResponseEntity<?> getCustomerProperties(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(customerService.getMyProperties(user.getId()));
    }

    @GetMapping("/on-going-projects")
    public ResponseEntity<?> getCustomerOnGoingProjects(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(customerService.onGoingProjects(user.getId()));
    }

    @GetMapping("/completed-projects")
    public ResponseEntity<?> getCustomerCompletedProjects(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(customerService.completedProjects(user.getId()));
    }
}

