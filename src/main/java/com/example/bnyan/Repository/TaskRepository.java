package com.example.bnyan.Repository;

import com.example.bnyan.Model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    Task findTaskById(Integer id);

    List<Task> getTasksByProjectId(Integer projectId);

    List<Task> getTasksByStatus(String status);

    List<Task> getTasksByEndDateBetween(LocalDateTime start, LocalDateTime end);

    List<Task> getTasksByEndDateBeforeAndStatusNot(LocalDateTime date, String status);

    List<Task> getTasksByEndDateBetweenAndStatusNot(LocalDateTime start, LocalDateTime end, String status);

    List<Task> getTasksByStartDateBetween(LocalDateTime start, LocalDateTime end);

    List<Task> getTasksByProject_ProjectManager_Id(Integer managerId);

}