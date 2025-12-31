package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.DTO.SpecialistRequestAutoFillDTO;
import com.example.bnyan.Model.SpecialistRequest;
import com.example.bnyan.Model.User;
import com.example.bnyan.Service.SpecialistRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    //admin
    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(200).body(specialistRequestService.getAll());
    }

    //customer
    @PostMapping("/add/{project_id}/{spec_id}")
    public ResponseEntity addSpecialistRequest(@AuthenticationPrincipal User user,
                                               @PathVariable Integer project_id,
                                               @PathVariable Integer spec_id,
                                               @Valid @RequestBody SpecialistRequest requestBody) {
        specialistRequestService.addSpecialistRequest(user.getId(), project_id, spec_id, requestBody);
        return ResponseEntity.status(200).body(new ApiResponse("Specialist request added successfully"));
    }
    //specialist
    @PutMapping("/accept/{requestId}")
    public ResponseEntity<?> acceptRequest(@AuthenticationPrincipal User user, @PathVariable Integer requestId) {
        specialistRequestService.acceptRequest(user.getId(),requestId);
        return ResponseEntity.status(200).body(new ApiResponse("Specialist request accepted"));
    }

    //customer
    @PutMapping("/add-manager/{project_id}/{manager_id}")
    public ResponseEntity<?> addManagerRequest(@AuthenticationPrincipal User user,@PathVariable Integer project_id, @PathVariable Integer manager_id, @RequestBody SpecialistRequest request) {
        specialistRequestService.addManagerRequest(user.getId(),request, project_id, manager_id);
        return ResponseEntity.ok(new ApiResponse("request added successfully"));
    }

    //specialist
    @PutMapping("/reject/{requestId}")
    public ResponseEntity<?> rejectRequest(@AuthenticationPrincipal User user,@PathVariable Integer requestId) {
        specialistRequestService.rejectRequest(user.getId(), requestId);
        return ResponseEntity.status(200).body(new ApiResponse("Specialist request rejected"));
    }

    //customer
    @PutMapping("/update/{requestId}")
    public ResponseEntity<?> updateRequest(@AuthenticationPrincipal User user,@PathVariable Integer requestId,
                                           @RequestBody @Valid SpecialistRequest request) {
        specialistRequestService.updateRequest(user.getId(), requestId, request);
        return ResponseEntity.status(200).body(new ApiResponse("Specialist request updated"));
    }

    //customer
    @DeleteMapping("/delete/{requestId}")
    public ResponseEntity<?> deleteRequest(@AuthenticationPrincipal User user, @PathVariable Integer requestId) {
        specialistRequestService.deleteRequest(user.getId(), requestId);
        return ResponseEntity.status(200).body(new ApiResponse("Specialist request deleted"));
    }

    //customer
    @PostMapping("/auto-fill/{project_id}")
    public ResponseEntity<? > autoFillSpecialistRequest(@AuthenticationPrincipal User user, @PathVariable Integer project_id, @RequestParam String specialistType) {
        SpecialistRequestAutoFillDTO autoFill = specialistRequestService.autoFillRequest(user.getId(), project_id, specialistType);
        return ResponseEntity.status(200).body(autoFill);
    }
}