package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.DTO.ProjectDTO;
import com.example.bnyan.Model.BuildRequest;
import com.example.bnyan.Model.User;
import com.example.bnyan.Service.BuildRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/build-request")
@RequiredArgsConstructor
public class BuildRequestController {

    private final BuildRequestService buildRequestService;

    /// CRUD endpoints

    @GetMapping("/get-all")
    public ResponseEntity<List<BuildRequest>> getAll(@AuthenticationPrincipal User authUser) {
        return ResponseEntity.ok(buildRequestService.getAllBuildRequests(authUser));
    }

    @PostMapping("/add/{landId}")
    public ResponseEntity<ApiResponse> add(
            @AuthenticationPrincipal User authUser,
            @PathVariable Integer landId, @RequestBody ProjectDTO projectDTO) {

        buildRequestService.add(authUser, landId, projectDTO);
        return ResponseEntity.ok(new ApiResponse("Build request added"));
    }

    @PutMapping("/update-status/{requestId}/{status}")
    public ResponseEntity<ApiResponse> updateStatus(
            @AuthenticationPrincipal User authUser,
            @PathVariable Integer requestId,
            @PathVariable String status) {

        buildRequestService.updateStatus(authUser, requestId, status);
        return ResponseEntity.ok(new ApiResponse("Build request status updated"));
    }

    @DeleteMapping("/delete/{requestId}")
    public ResponseEntity<ApiResponse> delete(
            @AuthenticationPrincipal User authUser,
            @PathVariable Integer requestId) {

        buildRequestService.delete(authUser, requestId);
        return ResponseEntity.ok(new ApiResponse("Build request deleted"));
    }

    /// Extra endpoints

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<BuildRequest> getById(
            @AuthenticationPrincipal User authUser,
            @PathVariable Integer id) {

        return ResponseEntity.ok(buildRequestService.getBuildRequestById(authUser, id));
    }

    @GetMapping("/get-by-status/{status}")
    public ResponseEntity<List<BuildRequest>> getByStatus(
            @AuthenticationPrincipal User authUser,
            @PathVariable String status) {

        return ResponseEntity.ok(buildRequestService.getBuildRequestsByStatus(authUser, status));
    }

    @GetMapping("/my-build-requests")
    public ResponseEntity<List<BuildRequest>> getMyRequests(@AuthenticationPrincipal User authUser) {
        return ResponseEntity.ok(buildRequestService.getMyBuildRequests(authUser));
    }

    @GetMapping("/get-by-land/{landId}")
    public ResponseEntity<List<BuildRequest>> getByLand(
            @AuthenticationPrincipal User authUser,
            @PathVariable Integer landId) {

        return ResponseEntity.ok(buildRequestService.getBuildRequestsByLandId(authUser, landId));
    }

    @PutMapping("/approve/{requestId}")
    public ResponseEntity<ApiResponse> approveRequest(
            @AuthenticationPrincipal User authUser,
            @PathVariable Integer requestId) {

        buildRequestService.approveRequest(authUser, requestId);
        return ResponseEntity.ok(new ApiResponse("Build request approved"));
    }
}
