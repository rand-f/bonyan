package com.example.bnyan.ServiceTest;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Project;
import com.example.bnyan.Model.ProjectManager;
import com.example.bnyan.Model.Task;
import com.example.bnyan.Service.TaskService;
import com.example.bnyan.Repository.ProjectManagerRepository;
import com.example.bnyan.Repository.ProjectRepository;
import com.example.bnyan.Repository.TaskRepository;
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

    private Task task1;
    private Project project;
    private ProjectManager manager;
    private List<Task> taskList;

    @BeforeEach
    void setup() {
        project = new Project();
        project.setId(1);

        manager = new ProjectManager();
        manager.setId(1);

        task1 = new Task();
        task1.setId(1);
        task1.setTitle("Initial Task");
        task1.setStatus("pending");
        task1.setEndDate(LocalDateTime.now().plusDays(1));

        taskList = new ArrayList<>();
        taskList.add(task1);
    }

    @Test
    public void getAllTaskTest() {
        when(taskRepository.findAll()).thenReturn(taskList);
        List<Task> result = taskService.getAllTask();
        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void addTaskSuccessTest() {
        when(projectRepository.findProjectById(1)).thenReturn(project);
        taskService.addTask(1, task1);
        verify(taskRepository, times(1)).save(task1);
        assertNotNull(task1.getProject());
    }

    @Test
    public void addTaskProjectNotFoundTest() {
        when(projectRepository.findProjectById(99)).thenReturn(null);
        assertThrows(ApiException.class, () -> taskService.addTask(99, task1));
    }

    @Test
    public void updateTaskSuccessTest() {
        when(taskRepository.findTaskById(1)).thenReturn(task1);

        Task updateData = new Task();
        updateData.setTitle("Updated Title");
        updateData.setStatus("completed");

        taskService.updateTask(1, updateData);

        assertEquals("Updated Title", task1.getTitle());
        assertEquals("completed", task1.getStatus());
        verify(taskRepository, times(1)).save(task1);
    }

    @Test
    public void deleteTaskSuccessTest() {
        when(taskRepository.findTaskById(1)).thenReturn(task1);
        taskService.deleteTask(1);
        verify(taskRepository, times(1)).delete(task1);
    }

    @Test
    public void getTasksByProjectSuccessTest() {
        when(projectRepository.findProjectById(1)).thenReturn(project);
        when(taskRepository.getTasksByProjectId(1)).thenReturn(taskList);

        List<Task> result = taskService.getTasksByProject(1);
        assertFalse(result.isEmpty());
    }

    @Test
    public void getTasksDueTodayTest() {
        when(taskRepository.getTasksByEndDateBetween(any(), any())).thenReturn(taskList);
        List<Task> result = taskService.getTasksDueToday();
        assertEquals(1, result.size());
    }

    @Test
    public void getOverdueTasksTest() {
        when(taskRepository.getTasksByEndDateBeforeAndStatusNot(any(), eq("completed"))).thenReturn(taskList);
        List<Task> result = taskService.getOverdueTasks();
        assertFalse(result.isEmpty());
    }

    @Test
    public void getTasksByManagerSuccessTest() {
        when(projectManagerRepository.findProjectManagerById(1)).thenReturn(manager);
        when(taskRepository.getTasksByProject_ProjectManager_Id(1)).thenReturn(taskList);

        List<Task> result = taskService.getTasksByManager(1);
        assertEquals(1, result.size());
    }
}