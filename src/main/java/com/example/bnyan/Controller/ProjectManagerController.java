package com.example.bnyan.Controller;
import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.DTO.ProjectManagerDTO;
import com.example.bnyan.Service.ProjectManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/project-manager")
@RequiredArgsConstructor
public class ProjectManagerController {

    private final ProjectManagerService projectManagerService;

    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(projectManagerService.getAll());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerManager(@RequestBody ProjectManagerDTO managerDTO) {
        projectManagerService.registerManager(managerDTO);
        return ResponseEntity.ok(new ApiResponse("project manager registered successfully"));
    }

    @PutMapping("/accept-request/{manager_id}/{request_id}")
    public ResponseEntity<?> acceptRequest(@PathVariable Integer manager_id, @PathVariable Integer request_id) {
        projectManagerService.exceptRequest(manager_id, request_id);
        return ResponseEntity.ok(new ApiResponse("request accepted successfully"));
    }

    @PutMapping("/reject-request/{manager_id}/{request_id}")
    public ResponseEntity<?> rejectRequest(@PathVariable Integer manager_id, @PathVariable Integer request_id) {
        projectManagerService.rejectRequest(manager_id, request_id);
        return ResponseEntity.ok(new ApiResponse("request rejected successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteManager(@PathVariable Integer id) {
        projectManagerService.deleteProjectManager(id);
        return ResponseEntity.ok(new ApiResponse("project manager deleted successfully"));
    }
}
