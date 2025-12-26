package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Customer;
import com.example.bnyan.Model.Project;
import com.example.bnyan.Repository.CustomerRepository;
import com.example.bnyan.Repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CustomerRepository customerRepository;

    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    public void addProject(Integer customer_id,Project project) {
        Customer customer= customerRepository.getCustomerById(customer_id);
        project.setCustomer(customer);
        projectRepository.save(project);
    }

    public void updateProject(Integer id, Project project) {
        Project old = projectRepository.findProjectById(id);
        if (old == null) {
            throw new ApiException("project not found");
        }

        old.setBudget(project.getBudget());
        old.setDescription(project.getDescription());
        old.setStartDate(project.getStartDate());
        old.setExpectedEndDate(project.getExpectedEndDate());

        projectRepository.save(old);
    }

    public void deleteProject(Integer id) {
        Project project = projectRepository.findProjectById(id);
        if (project == null) {
            throw new ApiException("project not found");
        }
        projectRepository.delete(project);
    }


}
