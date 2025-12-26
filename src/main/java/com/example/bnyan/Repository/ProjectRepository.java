package com.example.bnyan.Repository;

import com.example.bnyan.Model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {

    Project findProjectById(Integer id);
}

