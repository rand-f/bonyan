package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.DTO.ProjectAIDTO;
import com.example.bnyan.DTO.SpecialistRequestAutoFillDTO;
import com.example.bnyan.Model.*;
import com.example.bnyan.OpenAI.AiService;
import com.example.bnyan.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialistRequestService {

    private final SpecialistRequestRepository requestRepository;
    private final SpecialistRepository specialistRepository;
    private final ProjectRepository projectRepository;
    private final ProjectManagerRepository projectManagerRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final AiService aiService;
    private final BuildRequestRepository buildRequestRepository;

    public List<SpecialistRequest> getAll() {
        return requestRepository.findAll();
    }

    public void addSpecialistRequest(Integer user_id, Integer project_id, Integer spec_id, SpecialistRequest requestBody) {
        User user = userRepository.getUserById(user_id);
        if (user == null) {
            throw new ApiException("user not found");
        }

        Customer customer = customerRepository.getCustomerById(user_id);
        if (customer == null) {
            throw new ApiException("only customer can make this process");
        }

        Project project = projectRepository.findProjectById(project_id);
        if (project == null) {
            throw new ApiException("project not found");
        }

        if (project.getCustomer().getId() != customer.getId()) {
            throw new ApiException("unauthorized access");
        }

        Specialist specialist = specialistRepository.findSpecialistById(spec_id);
        if (specialist == null) {
            throw new ApiException("specialist not found");
        }

        // Create and save the request with ALL required fields from requestBody
        SpecialistRequest request = new SpecialistRequest();
        request.setProject(project);
        request.setSpecialist(specialist);
        request.setProjectExpectedEndDate(project.getExpectedEndDate());
        request.setCreated_at(LocalDateTime.now());
        request.setStatus("pending");

        // Get the required validated fields from requestBody
        request.setDescription(requestBody.getDescription());
        request.setExpectedStartDate(requestBody.getExpectedStartDate());
        request.setOfferedPrice(requestBody.getOfferedPrice());

        // Save the request first to generate its ID
        request = requestRepository.save(request);

        // Now add the request to the collections (bidirectional relationship)
        project.getRequests().add(request);
        specialist.getRequests().add(request);

        // Save the parent entities
        projectRepository.save(project);
        specialistRepository.save(specialist);

        sendSpecialistRequestNotification(specialist, project, request);
    }
    public void acceptRequest(Integer user_id,Integer requestId) {
        User user = userRepository.getUserById(user_id);
            if(user==null){
                throw new ApiException("user not found");
            }

        SpecialistRequest specialistRequest = requestRepository.findSpecialistRequestById(requestId);
        if (specialistRequest == null) {
            throw new ApiException("Specialist request not found");
        }

        Specialist specialist = specialistRequest.getSpecialist();

        if(specialist.getId()!=user.getId()){
            throw new ApiException("unauthorized access");
        }

        if (!specialistRequest.getStatus().equalsIgnoreCase("pending")) {
            throw new ApiException("Only pending requests can be accepted");
        }



        Project project = specialistRequest.getProject();

        specialist.getProjects().add(project);
        project.getSpecialists().add(specialist);

        specialistRequest.setStatus("accepted");
        requestRepository.save(specialistRequest);
        specialistRepository.save(specialist);
        projectRepository.save(project);

        sendAcceptNotification(specialistRequest);
    }

    public void rejectRequest(Integer user_id,Integer requestId) {
        User user = userRepository.getUserById(user_id);
        if(user==null){
            throw new ApiException("user not found");
        }

        SpecialistRequest specialistRequest = requestRepository.findSpecialistRequestById(requestId);
        if (specialistRequest == null) {
            throw new ApiException("Specialist request not found");
        }

        Specialist specialist = specialistRequest.getSpecialist();

        if(specialist.getId()!=user.getId()){
            throw new ApiException("unauthorized access");
        }

        if (!specialistRequest.getStatus().equalsIgnoreCase("pending")) {
            throw new ApiException("Only pending requests can be rejected");
        }

        specialistRequest.setStatus("rejected");
        requestRepository.save(specialistRequest);

        sendRejectNotification(specialistRequest);
    }

    public void addManagerRequest(Integer user_id,SpecialistRequest request, Integer project_id, Integer manager_id) {
        User user = userRepository.getUserById(user_id);
        if(user==null){
            throw new ApiException("user not found");
        }

        Customer customer = customerRepository.getCustomerById(user_id);
        if(customer==null){
            throw new ApiException("you need to sign in as a customer to continue");
        }

        Project project = projectRepository.findProjectById(project_id);

        if(project.getCustomer().getId()!=customer.getId()){
            throw new ApiException("unauthorized access");
        }

        ProjectManager manager = projectManagerRepository.findProjectManagerById(manager_id);

        if(project==null || manager==null){
            throw new ApiException("this project request can not be assigned to the specialist");
        }

        request.setProject(project);
        project.getRequests().add(request);
        request.setProjectManager(manager);
        manager.getRequests().add(request);

        projectRepository.save(project);
        projectManagerRepository.save(manager);

        request.setProjectExpectedEndDate(project.getExpectedEndDate());
        request.setCreated_at(LocalDateTime.now());
        request.setStatus("pending");
        requestRepository.save(request);
    }

    public void updateRequest(Integer user_id, Integer id, SpecialistRequest request) {
        User user = userRepository.getUserById(user_id);
        if(user==null){
            throw new ApiException("user not found");
        }

        Customer customer = customerRepository.getCustomerById(user_id);
        if(customer==null){
            throw new ApiException("you need to sign in as a customer to continue");
        }

        SpecialistRequest old = requestRepository.findSpecialistRequestById(id);
        if (old == null) {
            throw new ApiException("request not found");
        }

        if(old.getProject().getCustomer().getId()!= customer.getId()){
            throw new ApiException("unauthorized access");
        }

        old.setDescription(request.getDescription());
        old.setOfferedPrice(request.getOfferedPrice());
        //old.setStatus(request.getStatus());
        old.setExpectedStartDate(request.getExpectedStartDate());
        old.setProjectExpectedEndDate(request.getProjectExpectedEndDate());

        requestRepository.save(old);
    }

    public void deleteRequest(Integer user_id,Integer id) {
        User user = userRepository.getUserById(user_id);
        if(user==null){
            throw new ApiException("user not found");
        }

        Customer customer = customerRepository.getCustomerById(user_id);
        if(customer==null){
            throw new ApiException("you need to sign in as a customer to continue");
        }

        SpecialistRequest request = requestRepository.findSpecialistRequestById(id);
        if (request == null) {
            throw new ApiException("request not found");
        }

        if(request.getProject().getCustomer().getId()!= customer.getId()){
            throw new ApiException("unauthorized access");
        }

        requestRepository.delete(request);
    }

    // n8n Webhooks
    private void sendSpecialistRequestNotification(Specialist specialist, Project project, SpecialistRequest request) {
        String webhookUrl = "http://localhost:5678/webhook-test/specialist-request-created";

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("requestId", request.getId());
        payload.put("requestDescription", request.getDescription());
        payload.put("requestStatus", request.getStatus());
        payload.put("offeredPrice", request.getOfferedPrice());
        payload.put("expectedStartDate", request.getExpectedStartDate() != null ? request.getExpectedStartDate().toString() : null);
        payload.put("projectExpectedEndDate", request.getProjectExpectedEndDate() != null ? request.getProjectExpectedEndDate().toString() : null);
        payload.put("specialistName", specialist.getUser().getUsername());
        payload.put("specialistEmail", specialist.getUser().getEmail());
        payload.put("specialistPhone", specialist.getUser().getPhoneNumber());
        payload.put("specialistSpeciality", specialist.getSpeciality());
        payload.put("projectName", project.getDescription());
        payload.put("projectBudget", project.getBudget());
        payload.put("projectStage", project.getTask());
        payload.put("createdAt", request.getCreated_at().toString());

        try {
            new RestTemplate().postForObject(webhookUrl, payload, String.class);
        } catch (Exception e) {
            System.out.println("Failed to send webhook: " + e.getMessage());
        }
    }

    private void sendAcceptNotification(SpecialistRequest request) {
        String webhookUrl = "http://localhost:5678/webhook-test/specialist-request-accepted";

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("requestId", request.getId());
        payload.put("requestDescription", request.getDescription());
        payload.put("offeredPrice", request.getOfferedPrice());
        payload.put("specialistName", request.getSpecialist().getUser().getUsername());
        payload.put("specialistEmail", request.getSpecialist().getUser().getEmail());
        payload.put("specialistPhone", request.getSpecialist().getUser().getPhoneNumber());
        payload.put("specialistSpeciality", request.getSpecialist().getSpeciality());
        payload.put("projectName", request.getProject().getDescription());
        payload.put("projectBudget", request.getProject().getBudget());
        payload.put("acceptedAt", LocalDateTime.now().toString());

        try {
            new RestTemplate().postForObject(webhookUrl, payload, String.class);
        } catch (Exception e) {
            System.out.println("Failed to send webhook: " + e.getMessage());
        }
    }

    private void sendRejectNotification(SpecialistRequest request) {
        String webhookUrl = "http://localhost:5678/webhook-test/specialist-request-rejected";

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("requestId", request.getId());
        payload.put("requestDescription", request.getDescription());
        payload.put("offeredPrice", request.getOfferedPrice());
        payload.put("specialistName", request.getSpecialist().getUser().getUsername());
        payload.put("specialistEmail", request.getSpecialist().getUser().getEmail());
        payload.put("specialistPhone", request.getSpecialist().getUser().getPhoneNumber());
        payload.put("specialistSpeciality", request.getSpecialist().getSpeciality());
        payload.put("projectName", request.getProject().getDescription());
        payload.put("projectBudget", request.getProject().getBudget());
        payload.put("rejectedAt", LocalDateTime.now().toString());

        try {
            new RestTemplate().postForObject(webhookUrl, payload, String.class);
        } catch (Exception e) {
            System.out.println("Failed to send webhook: " + e.getMessage());
        }
    }

    public SpecialistRequestAutoFillDTO autoFillRequest(Integer user_id, Integer project_id, String specialistType) {
        User user = userRepository.getUserById(user_id);
        if (user == null) {
            throw new ApiException("user not found");
        }

        Customer customer = customerRepository.getCustomerById(user_id);
        if (customer == null) {
            throw new ApiException("only customer can make this process");
        }

        Project project = projectRepository.findProjectById(project_id);
        if (project == null) {
            throw new ApiException("project not found");
        }

        if (project.getCustomer().getId() != customer.getId()) {
            throw new ApiException("unauthorized access");
        }

        // Get location and land size from BuildRequest
        BuildRequest buildRequest = buildRequestRepository.getBuildRequestByProjectId(project_id);
        String location = "غير محدد";
        String landSize = "غير محدد";

        if (buildRequest != null && buildRequest. getLand() != null) {
            location = buildRequest.getLand().getLocation();
            landSize = buildRequest.getLand().getSize();
        }

        ProjectAIDTO aiDTO = new ProjectAIDTO();
        aiDTO.setDescription(project.getDescription());
        aiDTO.setBudget(project.getBudget());
        aiDTO.setLocation(location);
        aiDTO.setLandSize(landSize);

        return aiService.autoFillSpecialistRequest(aiDTO, specialistType);
    }

}