package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Task;
import com.example.bnyan.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> get() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            throw new ApiException("No tasks found");
        }
        return tasks;
    }

    public void add(Task task) {
        taskRepository.save(task);
    }

    public void updateStatus(Integer taskId, String status) {
        Task task = taskRepository.getTaskById(taskId);
        if (task == null) {
            throw new ApiException("Task not found");
        }
        task.setStatus(status);
        taskRepository.save(task);
    }

    public void delete(Integer taskId) {
        Task task = taskRepository.getTaskById(taskId);
        if (task == null) {
            throw new ApiException("Task not found");
        }
        taskRepository.delete(task);
    }

    ///  extra endpoints


   public Task getTaskById(Integer id) {
        Task task = taskRepository.getTaskById(id);
        if (task == null) {
            throw new ApiException("Task not found");
        }
        return task;
    }




}
