package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Project;
import com.example.bnyan.Model.Specialist;
import com.example.bnyan.Model.SpecialistRequest;
import com.example.bnyan.Repository.ProjectRepository;
import com.example.bnyan.Repository.SpecialistRepository;
import com.example.bnyan.Repository.SpecialistRequestRepository;
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

    public List<SpecialistRequest> getAll() {
        return requestRepository.findAll();
    }

    public void addRequest(SpecialistRequest request, Integer project_id, Integer spec_id) {
        Project project = projectRepository.findProjectById(project_id);
        Specialist specialist = specialistRepository.findSpecialistById(spec_id);

        if (project == null || specialist == null) {
            throw new ApiException("this project request can not be assigned to the specialist");
        }

        request.setProject(project);
        project.getRequests().add(request);
        request.setSpecialist(specialist);
        specialist.getRequests().add(request);

        projectRepository.save(project);
        specialistRepository.save(specialist);

        request.setProjectExpectedEndDate(project.getExpectedEndDate());
        request.setCreated_at(LocalDateTime.now());
        request.setStatus("pending");
        requestRepository.save(request);

        sendSpecialistRequestNotification(specialist, project, request);
    }

    public void acceptRequest(Integer requestId) {
        SpecialistRequest specialistRequest = requestRepository.findSpecialistRequestById(requestId);
        if (specialistRequest == null) {
            throw new ApiException("Specialist request not found");
        }

        if (!specialistRequest.getStatus().equalsIgnoreCase("pending")) {
            throw new ApiException("Only pending requests can be accepted");
        }

        Specialist specialist = specialistRequest.getSpecialist();
        Project project = specialistRequest.getProject();

        specialist.getProjects().add(project);
        project.getSpecialists().add(specialist);

        specialistRequest.setStatus("accepted");
        requestRepository.save(specialistRequest);
        specialistRepository.save(specialist);
        projectRepository.save(project);

        sendAcceptNotification(specialistRequest);
    }

    public void rejectRequest(Integer requestId) {
        SpecialistRequest specialistRequest = requestRepository.findSpecialistRequestById(requestId);
        if (specialistRequest == null) {
            throw new ApiException("Specialist request not found");
        }

        if (!specialistRequest.getStatus().equalsIgnoreCase("pending")) {
            throw new ApiException("Only pending requests can be rejected");
        }

        specialistRequest.setStatus("rejected");
        requestRepository.save(specialistRequest);

        sendRejectNotification(specialistRequest);
    }

    public void updateRequest(Integer id, SpecialistRequest request) {
        SpecialistRequest old = requestRepository.findSpecialistRequestById(id);
        if (old == null) {
            throw new ApiException("request not found");
        }

        old.setDescription(request.getDescription());
        old.setOfferedPrice(request.getOfferedPrice());
        old.setStatus(request.getStatus());
        old.setExpectedStartDate(request.getExpectedStartDate());
        old.setProjectExpectedEndDate(request.getProjectExpectedEndDate());

        requestRepository.save(old);
    }

    public void deleteRequest(Integer id) {
        SpecialistRequest request = requestRepository.findSpecialistRequestById(id);
        if (request == null) {
            throw new ApiException("request not found");
        }
        requestRepository.delete(request);
    }

    // n8n Webhooks
    private void sendSpecialistRequestNotification(Specialist specialist, Project project, SpecialistRequest request) {
        String webhookUrl = "http://localhost:5678/webhook/specialist-request-created";

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
        payload.put("projectManagerName", project.getProjectManager().getUser().getUsername());
        payload.put("projectManagerEmail", project.getProjectManager().getUser().getEmail());
        payload.put("projectManagerPhone", project.getProjectManager().getUser().getPhoneNumber());
        payload.put("createdAt", request.getCreated_at().toString());

        try {
            new RestTemplate().postForObject(webhookUrl, payload, String.class);
        } catch (Exception e) {
            System.out.println("Failed to send webhook: " + e.getMessage());
        }
    }

    private void sendAcceptNotification(SpecialistRequest request) {
        String webhookUrl = "http://localhost:5678/webhook/specialist-request-accepted";

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
        payload.put("projectManagerName", request.getProject().getProjectManager().getUser().getUsername());
        payload.put("projectManagerEmail", request.getProject().getProjectManager().getUser().getEmail());
        payload.put("projectManagerPhone", request.getProject().getProjectManager().getUser().getPhoneNumber());
        payload.put("acceptedAt", LocalDateTime.now().toString());

        try {
            new RestTemplate().postForObject(webhookUrl, payload, String.class);
        } catch (Exception e) {
            System.out.println("Failed to send webhook: " + e.getMessage());
        }
    }

    private void sendRejectNotification(SpecialistRequest request) {
        String webhookUrl = "http://localhost:5678/webhook/specialist-request-rejected";

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
        payload.put("projectManagerName", request.getProject().getProjectManager().getUser().getUsername());
        payload.put("projectManagerEmail", request.getProject().getProjectManager().getUser().getEmail());
        payload.put("projectManagerPhone", request.getProject().getProjectManager().getUser().getPhoneNumber());
        payload.put("rejectedAt", LocalDateTime.now().toString());

        try {
            new RestTemplate().postForObject(webhookUrl, payload, String.class);
        } catch (Exception e) {
            System.out.println("Failed to send webhook: " + e.getMessage());
        }
    }
}