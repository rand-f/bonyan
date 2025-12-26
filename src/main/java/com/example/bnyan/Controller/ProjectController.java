package com.example.bnyan.Controller;
import com.example.bnyan.Model.Project;
import com.example.bnyan.Service.ProjectService;
import lombok.RequiredArgsConstructor;
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

    @PostMapping("/add/{customer_id}")
    public ResponseEntity<?> addProject(@PathVariable Integer customer_id,
                                        @RequestBody Project project) {
        projectService.addProject(customer_id, project);
        return ResponseEntity.ok(new ApiResponse("project added successfully"));
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
}
