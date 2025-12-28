package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.Model.Task;
import com.example.bnyan.Service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("api/v1/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /// CRUD endpoints

    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(200).body(taskService.getAllTask());
    }

    @PostMapping("/add/{projectId}")
    public ResponseEntity<?> add(@PathVariable Integer projectId, @RequestBody @Valid Task task) {

        taskService.addTask(projectId, task);
        return ResponseEntity.status(200).body(new ApiResponse("Task added"));
    }

    @PutMapping("/update/{taskId}")
    public ResponseEntity<?> update(@PathVariable Integer taskId, @RequestBody @Valid Task task) {

        taskService.updateTask(taskId, task);
        return ResponseEntity.status(200).body(new ApiResponse("Task updated"));
    }

    @DeleteMapping("/delete/{taskId}")
    public ResponseEntity<?> delete(@PathVariable Integer taskId) {

        taskService.deleteTask(taskId);
        return ResponseEntity.status(200).body(new ApiResponse("Task deleted"));
    }


    /// Extra endpoints


    @GetMapping("/get-by-project/{projectId}")
    public ResponseEntity<?> getByProject(@PathVariable Integer projectId) {
        return ResponseEntity.status(200).body(taskService.getTasksByProject(projectId));
    }

    @GetMapping("/get-by-status/{status}")
    public ResponseEntity<?> getByStatus(@PathVariable String status) {
        return ResponseEntity.status(200).body(taskService.getTasksByStatus(status));
    }

    @GetMapping("/get-due-today")
    public ResponseEntity<?> getDueToday() {
        return ResponseEntity.status(200).body(taskService.getTasksDueToday());
    }

    @GetMapping("/get-overdue")
    public ResponseEntity<?> getOverdue() {
        return ResponseEntity.status(200).body(taskService.getOverdueTasks());
    }

    @GetMapping("/get-upcoming")
    public ResponseEntity<?> getUpcoming() {
        return ResponseEntity.status(200).body(taskService.getUpcomingTasks());
    }

    @GetMapping("/get-by-date-range")
    public ResponseEntity<?> getByDateRange(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {

        return ResponseEntity.status(200).body(taskService.getTasksByDateRange(startDate, endDate));
    }
}
