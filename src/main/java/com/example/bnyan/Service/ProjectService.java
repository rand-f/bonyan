package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.DTO.PredictionBudgetDTO;
import com.example.bnyan.DTO.PredictionTimeDTO;
import com.example.bnyan.DTO.ProjectAIDTO;
import com.example.bnyan.DTO.ProjectDTO;
import com.example.bnyan.Model.*;
import com.example.bnyan.OpenAI.AiService;
import com.example.bnyan.Repository.BuildRequestRepository;
import com.example.bnyan.Repository.CustomerRepository;
import com.example.bnyan.Repository.ProjectRepository;
import com.example.bnyan.Repository.UserRepository;
import com.example.bnyan.Stability.ImageGenerationService;
import com.example.bnyan.Stability.PromptBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CustomerRepository customerRepository;
    private final BuildRequestRepository buildRequestRepository;
    private final PromptBuilder promptBuilder;
    private final ImageGenerationService imageGenerationService;
    private final AiService aiService;
    private final UserRepository userRepository;

    //only for admin
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    //for customers
    public List<Project> getMyProjects(Integer user_id){
        User user = userRepository.getUserById(user_id);
        Customer customer = customerRepository.getCustomerById(user_id);

        if (user==null||customer==null){
            throw new ApiException("Customer not found");
        }

        return projectRepository.findProjectsByCustomer(customer);
    }


    public void addProject(Integer user_id, Integer request_id, ProjectDTO projectDTO) {
        User user = userRepository.getUserById(user_id);
        Customer customer= customerRepository.getCustomerById(user_id);
        if(user==null||customer==null){
            throw new ApiException("Customer not found");
        }

        BuildRequest buildRequest= buildRequestRepository.getBuildRequestById(request_id);
        if(buildRequest==null){
            throw new ApiException("the request for this project does not exist");
        }

        if(buildRequest.getStatus().equals("approved")){
            throw new ApiException("can not start project because this project request is not approved");
        }

        Project project = buildRequest.getProject();
        if(project==null){
            throw new ApiException("project not found");
        }

        project.setCustomer(customer);

        ProjectAIDTO aiDTO = new ProjectAIDTO();
        aiDTO.setDescription(project.getDescription());
        aiDTO.setStartDate(project.getStartDate());
        aiDTO.setLandSize(buildRequest.getLand().getSize());
        aiDTO.setLocation(buildRequest.getLand().getLocation());


        // if user did not specify budget AI will
        if(projectDTO.getBudget()==null){
            PredictionBudgetDTO budgetDTO= predictBudget(user_id, project.getId(), aiDTO);
            project.setBudget(budgetDTO.getMaxBudget()+budgetDTO.getMinBudget()*0.5);
            aiDTO.setBudget(budgetDTO.getMaxBudget()+budgetDTO.getMinBudget()*0.5);
        }else{
            project.setBudget(projectDTO.getBudget());
            aiDTO.setBudget(projectDTO.getBudget());
        }

        // if user did not specify time AI will
        if(projectDTO.getProjectPeriod()==null){
            PredictionTimeDTO timeDTO = predictTime(user_id,project.getId(),aiDTO);
            project.setDuration(timeDTO.getExpectedProjectPeriod());
            project.setExpectedEndDate(projectDTO.getStartDate().plusDays(projectDTO.getProjectPeriod()));
        }else{
            project.setDuration(projectDTO.getProjectPeriod());
            project.setExpectedEndDate(projectDTO.getStartDate().plusDays(projectDTO.getProjectPeriod()));
        }

        project.setDescription(projectDTO.getDescription());
        project.setStartDate(projectDTO.getStartDate());

        project.setStatus("preparing");
        project.setCreated_at(LocalDateTime.now());
        projectRepository.save(project);
    }

    public PredictionBudgetDTO predictBudget(Integer customer_id, Integer project_id,ProjectAIDTO aiDTO){
        Customer customer= customerRepository.getCustomerById(customer_id);
        if(customer==null){
            throw new ApiException("Customer not found");
        }

        Project project = projectRepository.findProjectById(project_id);
        if(project==null){
            throw new ApiException("project not found");
        }

        if(project.getCustomer().getId()!=customer.getId()){
            throw new ApiException("unauthorized");
        }

        return aiService.predictBudget(aiDTO);
    }

    public PredictionTimeDTO predictTime(Integer customer_id,Integer project_id, ProjectAIDTO aiDTO){
        Customer customer= customerRepository.getCustomerById(customer_id);
        if(customer==null){
            throw new ApiException("Customer not found");
        }

        Project project = projectRepository.findProjectById(project_id);
        if(project==null){
            throw new ApiException("project not found");
        }

        if(project.getCustomer().getId()!=customer.getId()){
            throw new ApiException("unauthorized");
        }

        return aiService.predictTime(aiDTO);
    }

    public void updateProject(Integer user_id,Integer id, Project project) {
        User user = userRepository.getUserById(user_id);
        Customer customer= customerRepository.getCustomerById(user_id);
        if(user==null||customer==null){
            throw new ApiException("Customer not found");
        }

        Project old = projectRepository.findProjectById(id);
        if (old == null) {
            throw new ApiException("project not found");
        }

        if(old.getCustomer().getId()!=customer.getId()){
            throw new ApiException("unauthorized access");
        }

        old.setBudget(project.getBudget());
        old.setDescription(project.getDescription());
        old.setStartDate(project.getStartDate());
        old.setExpectedEndDate(project.getExpectedEndDate());

        projectRepository.save(old);
    }

    public void deleteProject(Integer user_id, Integer id) {
        User user = userRepository.getUserById(user_id);
        Customer customer= customerRepository.getCustomerById(user_id);
        if(user==null||customer==null){
            throw new ApiException("Customer not found");
        }

        Project project = projectRepository.findProjectById(id);
        if (project == null) {
            throw new ApiException("project not found");
        }

        if(project.getCustomer().getId()!=customer.getId()){
            throw new ApiException("unauthorized access");
        }

        projectRepository.delete(project);
    }

    public byte[] generateImage(Integer user_id, Integer project_id){
        User user = userRepository.getUserById(user_id);
        Customer customer= customerRepository.getCustomerById(user_id);
        if(user==null||customer==null){
            throw new ApiException("Customer not found");
        }

        Project project = projectRepository.findProjectById(project_id);
        if (project==null){
            throw new ApiException("project not found");
        }

        if(project.getCustomer().getId()!=customer.getId()){
            throw new ApiException("unauthorized access");
        }

        String prompt = promptBuilder.generateImagePrompt(project.getDescription());
        byte[] image= imageGenerationService.generateDraftBuilding(prompt);
        return image;
    }

    public ArrayList workingOnTheProject(Integer using_id, Integer project_id){
        User user = userRepository.getUserById(using_id);
        if (user==null){
            throw new ApiException("user not found");
        }

        Project project = projectRepository.findProjectById(project_id);
        if (project==null){
            throw new ApiException("project not found");
        }

        if(project.getCustomer().getId()!=user.getId() && !user.getRole().equals("ADMIN")){
            throw new ApiException("unauthorized");
        }

        if(project.getSpecialists().isEmpty()&&project.getProjectManager()==null){
            throw new ApiException("no one working on the project yet");
        }

        ArrayList workers= new ArrayList<>();

        for(Specialist specialist:project.getSpecialists()){
            workers.add(specialist);
        }
        workers.add(project.getProjectManager());

        return workers;
    }
}
