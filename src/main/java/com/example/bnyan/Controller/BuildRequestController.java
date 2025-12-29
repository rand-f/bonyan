package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.Model.BuildRequest;
import com.example.bnyan.Service.BuildRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/build-request")
@RequiredArgsConstructor
public class BuildRequestController {

    private final BuildRequestService buildRequestService;

    /// CRUD endpoints

    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(200).body(buildRequestService.getAllBuildRequests());
    }

    @PostMapping("/add/{customerId}/{landId}")
    public ResponseEntity<?> add(@PathVariable Integer customerId, @PathVariable Integer landId, @RequestBody @Valid BuildRequest buildRequest) {

        buildRequestService.add(customerId, landId, buildRequest);
        return ResponseEntity.status(200).body(new ApiResponse("Build request added"));
    }

    @PutMapping("/update-status/{requestId}/{adminId}/{status}")
    public ResponseEntity<?> updateStatus(@PathVariable Integer requestId, @PathVariable Integer adminId, @PathVariable String status) {

        buildRequestService.updateStatus(requestId, adminId, status);
        return ResponseEntity.status(200).body(new ApiResponse("Build request status updated"));
    }

    @DeleteMapping("/delete/{requestId}/{customerId}")
    public ResponseEntity<?> delete(@PathVariable Integer requestId, @PathVariable Integer customerId) {

        buildRequestService.delete(requestId, customerId);
        return ResponseEntity.status(200).body(new ApiResponse("Build request deleted"));
    }


    /// Extra endpoints (logic)

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(buildRequestService.getBuildRequestById(id));
    }

    @GetMapping("/get-by-status/{status}")
    public ResponseEntity<?> getByStatus(@PathVariable String status) {
        return ResponseEntity.status(200).body(buildRequestService.getBuildRequestsByStatus(status));
    }

    @GetMapping("/get-by-customer-id/{customerId}")
    public ResponseEntity<?> getByCustomerId(@PathVariable Integer customerId) {
        return ResponseEntity.status(200).body(buildRequestService.getBuildRequestsByCustomerId(customerId));
    }

    @GetMapping("/get-by-land-id/{landId}")
    public ResponseEntity<?> getByLandId(@PathVariable Integer landId) {
        return ResponseEntity.status(200).body(buildRequestService.getBuildRequestsByLandId(landId));
    }

    // for admin
    @PutMapping("/approve/{requestId}")
    public ResponseEntity<?> approveRequest(@PathVariable Integer requestId) {

        buildRequestService.approveRequest(requestId);
        return ResponseEntity.status(200).body(new ApiResponse("Build request is approved"));
    }
}
