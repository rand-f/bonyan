package com.example.bnyan.Repository;

import com.example.bnyan.Model.ProjectManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectManagerRepository extends JpaRepository<ProjectManager,Integer> {
    ProjectManager findProjectManagerById(Integer id);
}

