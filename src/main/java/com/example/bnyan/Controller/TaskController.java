package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.Model.Task;
import com.example.bnyan.Service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /// CRUD endpoints

    @GetMapping("/get-all")
    public ResponseEntity<List<Task>> getAll() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping("/add/{projectId}")
    public ResponseEntity<ApiResponse> add(@PathVariable Integer projectId, @RequestBody @Valid Task task) {
        taskService.addTask(projectId, task);
        return ResponseEntity.ok(new ApiResponse("Task added"));
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<ApiResponse> update(@PathVariable Integer taskId, @RequestBody @Valid Task task) {
        taskService.updateTask(taskId, task);
        return ResponseEntity.ok(new ApiResponse("Task updated"));
    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Integer taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok(new ApiResponse("Task deleted"));
    }

    /// Extra endpoints

    @GetMapping("/get-by-project/{projectId}")
    public ResponseEntity<List<Task>> getByProject(@PathVariable Integer projectId) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId));
    }

    @GetMapping("/get-by-status/{status}")
    public ResponseEntity<List<Task>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }

    @GetMapping("/get-due-today")
    public ResponseEntity<List<Task>> getDueToday() {
        return ResponseEntity.ok(taskService.getTasksDueToday());
    }

    @GetMapping("/get-overdue")
    public ResponseEntity<List<Task>> getOverdue() {
        return ResponseEntity.ok(taskService.getOverdueTasks());
    }

    @GetMapping("/get-upcoming")
    public ResponseEntity<List<Task>> getUpcoming() {
        return ResponseEntity.ok(taskService.getUpcomingTasks());
    }

    @GetMapping("/get-by-date-range")
    public ResponseEntity<List<Task>> getByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(taskService.getTasksByDateRange(startDate, endDate));
    }

    @GetMapping("/get-by-manager/{managerId}")
    public ResponseEntity<List<Task>> getByManager(@PathVariable Integer managerId) {
        return ResponseEntity.ok(taskService.getTasksByManager(managerId));
    }

    @GetMapping("/get-by-id/{taskId}")
    public ResponseEntity<Task> getById(@PathVariable Integer taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @GetMapping("/get-completed")
    public ResponseEntity<List<Task>> getCompleted() {
        return ResponseEntity.ok(taskService.getCompletedTasks());
    }
}
