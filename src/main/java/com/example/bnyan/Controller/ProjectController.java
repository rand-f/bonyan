package com.example.bnyan.Controller;
import com.example.bnyan.DTO.ProjectAIDTO;
import com.example.bnyan.DTO.ProjectDTO;
import com.example.bnyan.Model.Project;
import com.example.bnyan.Model.User;
import com.example.bnyan.Service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.bnyan.Api.ApiResponse;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    //admin
    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(projectService.getAll());
    }

    //customer
    @GetMapping("/get-my-projects")
    public ResponseEntity<?> getMyProjects(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(projectService.getMyProjects(user.getId()));
    }

    //customer
    @PostMapping("/add/{request_id}")
    public ResponseEntity<?> addProject(@AuthenticationPrincipal User user, @PathVariable Integer request_id,@RequestBody ProjectDTO project) {
        projectService.addProject(user.getId(), request_id, project);
        return ResponseEntity.ok(new ApiResponse("project added successfully"));
    }

    //customer
    @PostMapping("/budget/{project_id}")
    public ResponseEntity<?> projectBudget(@AuthenticationPrincipal User user,@PathVariable Integer project_id, @RequestBody ProjectAIDTO project) {
        return ResponseEntity.ok(projectService.predictBudget(user.getId(),project_id, project));
    }

    //customer
    @PostMapping("/time-prediction/{project_id}")
    public ResponseEntity<?> projectTime(@AuthenticationPrincipal User user,@PathVariable Integer project_id, @RequestBody ProjectAIDTO project) {
        return ResponseEntity.ok(projectService.predictBudget(user.getId(),project_id, project));
    }

    //customer
    @PutMapping("/update/{project_id}")
    public ResponseEntity<?> updateProject(@AuthenticationPrincipal User user,@PathVariable Integer project_id,
                                           @RequestBody Project project) {
        projectService.updateProject(user.getId(), project_id, project);
        return ResponseEntity.ok(new ApiResponse("project updated successfully"));
    }

    //customer
    @DeleteMapping("/delete/{project_id}")
    public ResponseEntity<?> deleteProject(@AuthenticationPrincipal User user, @PathVariable Integer project_id) {
        projectService.deleteProject(user.getId(),project_id);
        return ResponseEntity.ok(new ApiResponse("project deleted successfully"));
    }

    //customer
    @PostMapping("/generate-image/{project_id}")
    public ResponseEntity<?> generateDraft(@AuthenticationPrincipal User user,@PathVariable Integer project_id) {

        byte[] image = projectService.generateImage(user.getId(),project_id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"draft-building.jpeg\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    //customer + admin
    @GetMapping("/working-on-project/{project_id}")
    public ResponseEntity<?> getWorkingOnProject(@AuthenticationPrincipal User user,@PathVariable Integer project_id) {
        return ResponseEntity.ok(projectService.workingOnTheProject(user.getId(),project_id));
    }

}
