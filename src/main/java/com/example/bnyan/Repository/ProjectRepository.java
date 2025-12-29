package com.example.bnyan.Repository;

import com.example.bnyan.Model.Customer;
import com.example.bnyan.Model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Integer> {

    Project findProjectById(Integer id);

    List<Project> findProjectsByCustomer(Customer customer);

    List<Project> findProjectsByStatusAndStartDateBefore(String status, LocalDate date);

    List<Project> findProjectByCustomerAndStatus(Customer customer, String status);

    Project findProjectByCustomer(Customer customer);

}


