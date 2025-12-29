package com.example.bnyan.Controller;
import com.example.bnyan.DTO.ProjectAIDTO;
import com.example.bnyan.DTO.ProjectDTO;
import com.example.bnyan.Model.Project;
import com.example.bnyan.Service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.bnyan.Api.ApiResponse;

@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(projectService.getAll());
    }

    @GetMapping("/get-my-projects{customer_id}")
    public ResponseEntity<?> getMyProjects(@PathVariable Integer customer_id) {
        return ResponseEntity.ok(projectService.getMyProjects(customer_id));
    }

    @PostMapping("/add/{customer_id}/{request_id}")
    public ResponseEntity<?> addProject(@PathVariable Integer customer_id, @PathVariable Integer request_id,@RequestBody ProjectDTO project) {
        projectService.addProject(customer_id,request_id, project);
        return ResponseEntity.ok(new ApiResponse("project added successfully"));
    }

    @PostMapping("/budget/{customer_id}")
    public ResponseEntity<?> projectBudget(@PathVariable Integer customer_id, @RequestBody ProjectAIDTO project) {
        return ResponseEntity.ok(projectService.predictBudget(customer_id, project));
    }

    @PostMapping("/time-prediction/{customer_id}")
    public ResponseEntity<?> projectTime(@PathVariable Integer customer_id, @RequestBody ProjectAIDTO project) {
        return ResponseEntity.ok(projectService.predictBudget(customer_id, project));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Integer id,
                                           @RequestBody Project project) {
        projectService.updateProject(id, project);
        return ResponseEntity.ok(new ApiResponse("project updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Integer id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok(new ApiResponse("project deleted successfully"));
    }

    @PostMapping("/generate-image/{project_id}")
    public ResponseEntity<?> generateDraft(@PathVariable Integer project_id) {

        byte[] image = projectService.generateImage(project_id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"draft-building.jpeg\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }

    @GetMapping("/working-on-project/{project_id}")
    public ResponseEntity<?> getWorkingOnProject(@PathVariable Integer project_id) {
        return ResponseEntity.ok(projectService.workingOnTheProject(project_id));
    }
}
