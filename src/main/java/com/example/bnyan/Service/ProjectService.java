package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.DTO.ProjectDTO;
import com.example.bnyan.Model.BuildRequest;
import com.example.bnyan.Model.Customer;
import com.example.bnyan.Model.Project;
import com.example.bnyan.Repository.BuildRequestRepository;
import com.example.bnyan.Repository.CustomerRepository;
import com.example.bnyan.Repository.ProjectRepository;
import com.example.bnyan.Stability.ImageGenerationService;
import com.example.bnyan.Stability.PromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CustomerRepository customerRepository;
    private final BuildRequestRepository buildRequestRepository;
    private final PromptBuilder promptBuilder;
    private final ImageGenerationService imageGenerationService;

    //only for admin
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    //for customers
    public List<Project> getMyProjects(Integer customer_id){
        Customer customer = customerRepository.getCustomerById(customer_id);

        if (customer==null){
            throw new ApiException("Customer not found");
        }

        return projectRepository.findProjectsByCustomer(customer);
    }

    public void addProject(Integer customer_id, Integer request_id, ProjectDTO projectDTO) {

        Customer customer= customerRepository.getCustomerById(customer_id);
        if(customer==null){
            throw new ApiException("Customer not found");
        }

        BuildRequest buildRequest= buildRequestRepository.getBuildRequestById(request_id);
        if(buildRequest==null){
            throw new ApiException("the request for this project does not exist");
        }

        if(buildRequest.getStatus().equals("approved")){
            throw new ApiException("can not start project because this project request is not approved");
        }

        Project project = new Project();

        project.setBudget(projectDTO.getBudget());
        project.setDescription(projectDTO.getDescription());
        project.setStartDate(projectDTO.getStartDate());

        project.setCustomer(customer);
        project.setCreated_at(LocalDateTime.now());
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

    public byte[] generateImage(Integer project_id){
        Project project = projectRepository.findProjectById(project_id);
        if (project==null){
            throw new ApiException("project not found");
        }

        String prompt = promptBuilder.generateImagePrompt(project.getDescription());
        byte[] image= imageGenerationService.generateDraftBuilding(prompt);
        return image;
    }
}
