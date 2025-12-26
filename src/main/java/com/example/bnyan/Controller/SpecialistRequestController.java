package com.example.bnyan.Controller;

import com.example.bnyan.Model.SpecialistRequest;
import com.example.bnyan.Service.SpecialistRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.bnyan.Api.ApiResponse;

@RestController
@RequestMapping("/api/v1/specialist-request")
@RequiredArgsConstructor
public class SpecialistRequestController {

    private final SpecialistRequestService requestService;

    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(requestService.getAll());
    }

    @PostMapping("/add/{project_id}/{spec_id}")
    public ResponseEntity<?> addRequest(@PathVariable Integer project_id,
                                        @PathVariable Integer spec_id,
                                        @RequestBody SpecialistRequest request) {
        requestService.addRequest(request, project_id, spec_id);
        return ResponseEntity.ok(new ApiResponse("request added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRequest(@PathVariable Integer id,
                                           @RequestBody SpecialistRequest request) {
        requestService.updateRequest(id, request);
        return ResponseEntity.ok(new ApiResponse("request updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Integer id) {
        requestService.deleteRequest(id);
        return ResponseEntity.ok(new ApiResponse("request deleted successfully"));
    }
}
