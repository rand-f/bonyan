package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Project;
import com.example.bnyan.Model.ProjectManager;
import com.example.bnyan.Model.Task;
import com.example.bnyan.Model.User;
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

    /// CRUD

    public List<Task> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            throw new ApiException("No tasks found");
        }
        return tasks;
    }

    public void addTask(Integer projectId, Task task) {
        Project project = projectRepository.findProjectById(projectId);
        if (project == null) throw new ApiException("Project not found");

        task.setProject(project);
        task.setCreatedAt(LocalDateTime.now());
        taskRepository.save(task);
    }

    public void updateTask(Integer taskId, Task newTask) {
        Task task = taskRepository.findTaskById(taskId);
        if (task == null) throw new ApiException("Task not found");

        task.setTitle(newTask.getTitle());
        task.setStatus(newTask.getStatus());
        task.setStartDate(newTask.getStartDate());
        task.setEndDate(newTask.getEndDate());
        task.setBudget(newTask.getBudget());

        taskRepository.save(task);
    }

    public void deleteTask(Integer taskId) {
        Task task = taskRepository.findTaskById(taskId);
        if (task == null) throw new ApiException("Task not found");
        taskRepository.delete(task);
    }

    /// Extra endpoints

    public List<Task> getTasksByProject(Integer projectId) {
        Project project = projectRepository.findProjectById(projectId);
        if (project == null) throw new ApiException("Project not found");

        List<Task> tasks = taskRepository.getTasksByProjectId(projectId);
        if (tasks.isEmpty()) throw new ApiException("No tasks found for this project");

        return tasks;
    }

    public Task getTaskById(Integer taskId) {
        Task task = taskRepository.findTaskById(taskId);
        if (task == null) throw new ApiException("Task not found");
        return task;
    }

    public List<Task> getTasksByStatus(String status) {
        List<Task> tasks = taskRepository.getTasksByStatus(status);
        if (tasks.isEmpty()) throw new ApiException("No tasks found with status: " + status);
        return tasks;
    }

    public List<Task> getTasksDueToday() {
        LocalDateTime start = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime end = LocalDateTime.now().toLocalDate().atTime(23, 59, 59);

        List<Task> tasks = taskRepository.getTasksByEndDateBetween(start, end);
        if (tasks.isEmpty()) throw new ApiException("No tasks due today");
        return tasks;
    }

    public List<Task> getOverdueTasks() {
        LocalDateTime now = LocalDateTime.now();
        List<Task> tasks = taskRepository.getTasksByEndDateBeforeAndStatusNot(now, "completed");
        if (tasks.isEmpty()) throw new ApiException("No overdue tasks");
        return tasks;
    }

    public List<Task> getUpcomingTasks() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextWeek = now.plusDays(7);
        List<Task> tasks = taskRepository.getTasksByEndDateBetweenAndStatusNot(now, nextWeek, "completed");
        if (tasks.isEmpty()) throw new ApiException("No upcoming tasks in next 7 days");
        return tasks;
    }

    public List<Task> getCompletedTasks() {
        List<Task> tasks = taskRepository.getTasksByStatus("completed");
        if (tasks.isEmpty()) throw new ApiException("No completed tasks");
        return tasks;
    }

    public List<Task> getTasksByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Task> tasks = taskRepository.getTasksByStartDateBetween(startDate, endDate);
        if (tasks.isEmpty()) throw new ApiException("No tasks found in the specified date range");
        return tasks;
    }

    public List<Task> getTasksByManager(Integer managerId) {
        ProjectManager manager = projectManagerRepository.findProjectManagerById(managerId);
        if (manager == null) throw new ApiException("Project manager not found");

        List<Task> tasks = taskRepository.getTasksByProject_ProjectManager_Id(managerId);
        if (tasks.isEmpty()) throw new ApiException("No tasks found for this manager");
        return tasks;
    }
}
