package com.example.bnyan.ServiceTest;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Project;
import com.example.bnyan.Model.ProjectManager;
import com.example.bnyan.Model.Task;
import com.example.bnyan.Repository.ProjectManagerRepository;
import com.example.bnyan.Repository.ProjectRepository;
import com.example.bnyan.Repository.TaskRepository;
import com.example.bnyan.Service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private ProjectManagerRepository projectManagerRepository;

    private Project project;
    private Task task;

    @BeforeEach
    void setup() {
        project = new Project();
        project.setId(1);

        task = new Task();
        task.setId(100);
        task.setTitle("Test Task");
        task.setStatus("pending");
        task.setProject(project);
        task.setStartDate(LocalDateTime.now().minusDays(1));
        task.setEndDate(LocalDateTime.now().plusDays(1));
        task.setBudget(500.0);
    }

    @Test
    public void getAllTasksSuccessTest() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void getAllTasksEmptyTest() {
        when(taskRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(ApiException.class, () -> taskService.getAllTasks());
    }

    @Test
    public void addTaskSuccessTest() {
        when(projectRepository.findProjectById(project.getId())).thenReturn(project);

        Task newTask = new Task();
        taskService.addTask(project.getId(), newTask);

        assertEquals(project, newTask.getProject());
        assertNotNull(newTask.getCreatedAt());
        verify(taskRepository, times(1)).save(newTask);
    }

    @Test
    public void addTaskProjectNotFoundTest() {
        when(projectRepository.findProjectById(project.getId())).thenReturn(null);
        assertThrows(ApiException.class, () -> taskService.addTask(project.getId(), new Task()));
    }


    @Test
    public void updateTaskSuccessTest() {
        when(taskRepository.findTaskById(task.getId())).thenReturn(task);

        Task updated = new Task();
        updated.setTitle("Updated");
        updated.setStatus("in progress");
        updated.setStartDate(LocalDateTime.now());
        updated.setEndDate(LocalDateTime.now().plusDays(2));
        updated.setBudget(1000.0);

        taskService.updateTask(task.getId(), updated);

        assertEquals("Updated", task.getTitle());
        assertEquals("in progress", task.getStatus());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void updateTaskNotFoundTest() {
        when(taskRepository.findTaskById(task.getId())).thenReturn(null);
        assertThrows(ApiException.class, () -> taskService.updateTask(task.getId(), new Task()));
    }

    @Test
    public void deleteTaskSuccessTest() {
        when(taskRepository.findTaskById(task.getId())).thenReturn(task);

        taskService.deleteTask(task.getId());

        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    public void deleteTaskNotFoundTest() {
        when(taskRepository.findTaskById(task.getId())).thenReturn(null);
        assertThrows(ApiException.class, () -> taskService.deleteTask(task.getId()));
    }

    @Test
    public void getTasksByProjectSuccessTest() {
        when(projectRepository.findProjectById(project.getId())).thenReturn(project);
        when(taskRepository.getTasksByProjectId(project.getId())).thenReturn(List.of(task));

        List<Task> result = taskService.getTasksByProject(project.getId());
        assertEquals(1, result.size());
    }

    @Test
    public void getTasksByProjectNoTasksTest() {
        when(projectRepository.findProjectById(project.getId())).thenReturn(project);
        when(taskRepository.getTasksByProjectId(project.getId())).thenReturn(new ArrayList<>());
        assertThrows(ApiException.class, () -> taskService.getTasksByProject(project.getId()));
    }

    @Test
    public void getTasksByProjectNotFoundTest() {
        when(projectRepository.findProjectById(project.getId())).thenReturn(null);
        assertThrows(ApiException.class, () -> taskService.getTasksByProject(project.getId()));
    }

    @Test
    public void getTaskByIdSuccessTest() {
        when(taskRepository.findTaskById(task.getId())).thenReturn(task);

        Task result = taskService.getTaskById(task.getId());
        assertEquals(task, result);
    }

    @Test
    public void getTaskByIdNotFoundTest() {
        when(taskRepository.findTaskById(task.getId())).thenReturn(null);
        assertThrows(ApiException.class, () -> taskService.getTaskById(task.getId()));
    }

    @Test
    public void getTasksByStatusSuccessTest() {
        when(taskRepository.getTasksByStatus(task.getStatus())).thenReturn(List.of(task));

        List<Task> result = taskService.getTasksByStatus(task.getStatus());
        assertEquals(1, result.size());
    }

    @Test
    public void getTasksByStatusNoTasksTest() {
        when(taskRepository.getTasksByStatus(task.getStatus())).thenReturn(new ArrayList<>());
        assertThrows(ApiException.class, () -> taskService.getTasksByStatus(task.getStatus()));
    }

    @Test
    public void getTasksByManagerSuccessTest() {
        ProjectManager manager = new ProjectManager();
        manager.setId(50);
        when(projectManagerRepository.findProjectManagerById(manager.getId())).thenReturn(manager);
        when(taskRepository.getTasksByProject_ProjectManager_Id(manager.getId())).thenReturn(List.of(task));

        List<Task> result = taskService.getTasksByManager(manager.getId());
        assertEquals(1, result.size());
    }

    @Test
    public void getTasksByManagerNoTasksTest() {
        ProjectManager manager = new ProjectManager();
        manager.setId(50);
        when(projectManagerRepository.findProjectManagerById(manager.getId())).thenReturn(manager);
        when(taskRepository.getTasksByProject_ProjectManager_Id(manager.getId())).thenReturn(new ArrayList<>());

        assertThrows(ApiException.class, () -> taskService.getTasksByManager(manager.getId()));
    }

    @Test
    public void getTasksByManagerNotFoundTest() {
        when(projectManagerRepository.findProjectManagerById(50)).thenReturn(null);
        assertThrows(ApiException.class, () -> taskService.getTasksByManager(50));
    }
}