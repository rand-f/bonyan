package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.Model.UserRequest;
import com.example.bnyan.Service.UserRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user-request")
@RequiredArgsConstructor
public class UserRequestController {

    private final UserRequestService userRequestService;

    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return ResponseEntity.status(200).body(userRequestService.get());
    }

    @PostMapping("/add/{customerId}/{builtId}")
    public ResponseEntity<?> add(@PathVariable Integer customerId, @PathVariable Integer builtId, @RequestBody @Valid UserRequest userRequest) {
        userRequestService.add(customerId, builtId, userRequest);
        return ResponseEntity.status(200).body(new ApiResponse("User request added"));
    }

    @PutMapping("/update-status/{requestId}/{adminId}/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable Integer requestId, @PathVariable Integer adminId, @PathVariable String status) {
        userRequestService.updateStatus(requestId, adminId, status);
        return ResponseEntity.status(200).body(new ApiResponse("User request status updated"));
    }

    @DeleteMapping("/delete/{requestId}/{customerId}")
    public ResponseEntity<?> delete(@PathVariable Integer requestId, @PathVariable Integer customerId) {
        userRequestService.delete(requestId, customerId);
        return ResponseEntity.status(200).body(new ApiResponse("User request deleted"));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getUserRequestById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(userRequestService.getUserRequestById(id));
    }

    @GetMapping("/get-by-status/{status}")
    public ResponseEntity<?> getUserRequestsByStatus(@PathVariable String status) {
        return ResponseEntity.status(200).body(userRequestService.getUserRequestsByStatus(status));
    }

    @GetMapping("/get-by-type/{type}")
    public ResponseEntity<?> getUserRequestsByType(@PathVariable String type) {
        return ResponseEntity.status(200).body(userRequestService.getUserRequestsByType(type));
    }

    @GetMapping("/get-by-customer-id/{customerId}")
    public ResponseEntity<?> getUserRequestsByCustomerId(@PathVariable Integer customerId) {
        return ResponseEntity.status(200).body(userRequestService.getUserRequestsByCustomerId(customerId));
    }
}