package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.Model.SpecialistRequest;
import com.example.bnyan.Service.SpecialistRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/specialist-request")
@RequiredArgsConstructor
public class SpecialistRequestController {

    private final SpecialistRequestService specialistRequestService;

    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(200).body(specialistRequestService.getAll());
    }

    @PostMapping("/add/{projectId}/{specialistId}")
    public ResponseEntity<?> addRequest(@RequestBody @Valid SpecialistRequest request,
                                        @PathVariable Integer projectId,
                                        @PathVariable Integer specialistId) {
        specialistRequestService.addRequest(request, projectId, specialistId);
        return ResponseEntity.status(200).body(new ApiResponse("Specialist request added"));
    }

    @PutMapping("/accept/{requestId}")
    public ResponseEntity<?> acceptRequest(@PathVariable Integer requestId) {
        specialistRequestService.acceptRequest(requestId);
        return ResponseEntity.status(200).body(new ApiResponse("Specialist request accepted"));
    }

    @PostMapping("/add-specialist/{project_id}/{spec_id}")
    public ResponseEntity<?> addSpecialistRequest(@PathVariable Integer project_id, @PathVariable Integer spec_id, @RequestBody SpecialistRequest request) {
        requestService.addSpecialistRequest(request, project_id, spec_id);
        return ResponseEntity.ok(new ApiResponse("request added successfully"));
    }

    @PostMapping("/add-manager/{project_id}/{manager_id}")
    public ResponseEntity<?> addManagerRequest(@PathVariable Integer project_id, @PathVariable Integer manager_id, @RequestBody SpecialistRequest request) {
        requestService.addManagerRequest(request, project_id, manager_id);
        return ResponseEntity.ok(new ApiResponse("request added successfully"));
    @PutMapping("/reject/{requestId}")
    public ResponseEntity<?> rejectRequest(@PathVariable Integer requestId) {
        specialistRequestService.rejectRequest(requestId);
        return ResponseEntity.status(200).body(new ApiResponse("Specialist request rejected"));
    }

    @PutMapping("/update/{requestId}")
    public ResponseEntity<?> updateRequest(@PathVariable Integer requestId,
                                           @RequestBody @Valid SpecialistRequest request) {
        specialistRequestService.updateRequest(requestId, request);
        return ResponseEntity.status(200).body(new ApiResponse("Specialist request updated"));
    }

    @DeleteMapping("/delete/{requestId}")
    public ResponseEntity<?> deleteRequest(@PathVariable Integer requestId) {
        specialistRequestService.deleteRequest(requestId);
        return ResponseEntity.status(200).body(new ApiResponse("Specialist request deleted"));
    }
}