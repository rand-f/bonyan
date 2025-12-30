package com.example.bnyan.Controller;
import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.DTO.ProjectManagerDTO;
import com.example.bnyan.DTO.SpecialistDTO;
import com.example.bnyan.Model.User;
import com.example.bnyan.Service.ProjectManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project-manager")
@RequiredArgsConstructor
public class ProjectManagerController {

    private final ProjectManagerService projectManagerService;

    //admin and customer
    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(projectManagerService.getAll());
    }

    //everyone
    @PostMapping("/register")
    public ResponseEntity<?> registerManager(@RequestBody @Valid SpecialistDTO specialistDTO) {
        projectManagerService.registerManager(specialistDTO);
        return ResponseEntity.ok(new ApiResponse("project manager registered successfully"));
    }

    //manager
    @PutMapping("/accept-request/{request_id}")
    public ResponseEntity<?> acceptRequest(@AuthenticationPrincipal User user, @PathVariable Integer request_id) {
        projectManagerService.acceptRequest(user.getId(), request_id);
        return ResponseEntity.ok(new ApiResponse("request accepted successfully"));
    }

    //manager
    @PutMapping("/reject-request/{request_id}")
    public ResponseEntity<?> rejectRequest(@AuthenticationPrincipal User user, @PathVariable Integer request_id) {
        projectManagerService.rejectRequest(user.getId(), request_id);
        return ResponseEntity.ok(new ApiResponse("request rejected successfully"));
    }

    //admin
    @DeleteMapping("/delete/{manager_id}")
    public ResponseEntity<?> deleteManager(@PathVariable Integer manager_id) {
        projectManagerService.deleteProjectManager(manager_id);
        return ResponseEntity.ok(new ApiResponse("project manager deleted successfully"));
    }
}
