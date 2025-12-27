package com.example.bnyan.Repository;

import com.example.bnyan.Model.Customer;
import com.example.bnyan.Model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {

    Project findProjectById(Integer id);

    List<Project> findProjectsByCustomer(Customer customer);
}


