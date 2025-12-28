package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Project;
import com.example.bnyan.Model.ProjectManager;
import com.example.bnyan.Model.Task;
import com.example.bnyan.Repository.ProjectManagerRepository;
import com.example.bnyan.Repository.ProjectRepository;
import com.example.bnyan.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ProjectManagerRepository projectManagerRepository;

    /// CRUD operations

    public List<Task> getAllTask() {
        return taskRepository.findAll();
    }

    public void addTask(Integer projectId, Task task) {

        Project project = projectRepository.findProjectById(projectId);
        if (project == null) {
            throw new ApiException("Project not found");
        }

        task.setProject(project);
        task.setCreatedAt(LocalDateTime.now());

        taskRepository.save(task);
    }

    public void updateTask(Integer taskId, Task newTask) {

        Task task = taskRepository.findTaskById(taskId);
        if (task == null) {
            throw new ApiException("Task not found");
        }

        task.setTitle(newTask.getTitle());
        task.setStatus(newTask.getStatus());
        task.setStartDate(newTask.getStartDate());
        task.setEndDate(newTask.getEndDate());
        task.setBudget(newTask.getBudget());

        taskRepository.save(task);
    }

    public void deleteTask(Integer taskId) {

        Task task = taskRepository.findTaskById(taskId);
        if (task == null) {
            throw new ApiException("Task not found");
        }

        taskRepository.delete(task);
    }

    /// Extra endpoints (logic)


    // Tasks under a specific project
    public List<Task> getTasksByProject(Integer projectId) {

        Project project = projectRepository.findProjectById(projectId);
        if (project == null) {
            throw new ApiException("Project not found");
        }

        List<Task> tasks = taskRepository.getTasksByProjectId(projectId);
        if (tasks.isEmpty()) {
            throw new ApiException("No tasks found for this project");
        }

        return tasks;
    }

    // Get task by id
    public Task getTaskById(Integer taskId) {

        Task task = taskRepository.findTaskById(taskId);
        if (task == null) {
            throw new ApiException("Task not found");
        }

        return task;
    }

    // Tasks by status
    public List<Task> getTasksByStatus(String status) {

        List<Task> tasks = taskRepository.getTasksByStatus(status);
        if (tasks.isEmpty()) {
            throw new ApiException("No tasks found with status: " + status);
        }

        return tasks;
    }

    // Tasks due today
    public List<Task> getTasksDueToday() {

        LocalDateTime start = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime end = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);

        List<Task> tasks = taskRepository.getTasksByEndDateBetween(start, end);
        if (tasks.isEmpty()) {
            throw new ApiException("No tasks due today");
        }

        return tasks;
    }

    // Overdue tasks (not completed)
    public List<Task> getOverdueTasks() {

        LocalDateTime now = LocalDateTime.now();

        List<Task> tasks =
                taskRepository.getTasksByEndDateBeforeAndStatusNot(now, "completed");

        if (tasks.isEmpty()) {
            throw new ApiException("No overdue tasks");
        }

        return tasks;
    }

    // Upcoming tasks (next 7 days, not completed)
    public List<Task> getUpcomingTasks() {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextWeek = now.plusDays(7);

        List<Task> tasks =
                taskRepository.getTasksByEndDateBetweenAndStatusNot(now, nextWeek, "completed");

        if (tasks.isEmpty()) {
            throw new ApiException("No upcoming tasks in next 7 days");
        }

        return tasks;
    }

    // Completed tasks
    public List<Task> getCompletedTasks() {

        List<Task> tasks = taskRepository.getTasksByStatus("completed");
        if (tasks.isEmpty()) {
            throw new ApiException("No completed tasks");
        }

        return tasks;
    }

    // Tasks within date range
    public List<Task> getTasksByDateRange(LocalDateTime startDate, LocalDateTime endDate) {

        List<Task> tasks =
                taskRepository.getTasksByStartDateBetween(startDate, endDate);

        if (tasks.isEmpty()) {
            throw new ApiException("No tasks found within the specified date range");
        }

        return tasks;
    }


    // getTasksByManager
    public List<Task> getTasksByManager(Integer managerId) {

        ProjectManager manager = projectManagerRepository.findProjectManagerById(managerId);

        if (manager == null) {
            throw new ApiException("Project manager not found");
        }

        List<Task> tasks = taskRepository.getTasksByProject_ProjectManager_Id(managerId);

        if (tasks.isEmpty()) {
            throw new ApiException("No tasks found for this project manager");
        }

        return tasks;
    }


}
