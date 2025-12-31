package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.DTO.ProjectDTO;
import com.example.bnyan.Model.*;
import com.example.bnyan.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BuildRequestService {

    private final BuildRequestRepository buildRequestRepository;
    private final CustomerRepository customerRepository;
    private final LandRepository landRepository;
    private final ProjectRepository projectRepository;

    /// crud

    public List<BuildRequest> getAllBuildRequests(User authUser) {

        if (!authUser.getRole().equals("ADMIN")) {
            throw new ApiException("Only ADMIN can view all build requests");
        }

        List<BuildRequest> buildRequests = buildRequestRepository.findAll();

        if (buildRequests.isEmpty()) {
            throw new ApiException("No build requests found");
        }

        return buildRequests;
    }

    public void add(User authUser, Integer landId, ProjectDTO projectDTO) {

        if (!authUser.getRole().equals("USER")) {
            throw new ApiException("Only customers can create build requests");
        }

        Customer customer = customerRepository.getCustomerById(authUser.getId());
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        Land land = landRepository.getLandById(landId);
        if (land == null) {
            throw new ApiException("Land not found");
        }

        BuildRequest existing = buildRequestRepository.getBuildRequestByLandId(landId);
        if (existing != null) {
            throw new ApiException("Build request already exists for this land");
        }

        BuildRequest buildRequest = new BuildRequest();
        buildRequest.setCustomer(customer);
        buildRequest.setLand(land);
        buildRequest.setStatus("PROCESSING");
        buildRequest.setCreatedAt(LocalDateTime.now());

        Project project = new Project();
        project.setDescription(projectDTO.getDescription());
        project.setBudget(projectDTO.getBudget());
        project.setDuration(projectDTO.getDuration());
        project.setStartDate(projectDTO.getStartDate());
        project.setExpectedEndDate(projectDTO.getExpectedEndDate());
        project.setStatus("preparing");
        project.setCreated_at(LocalDateTime.now());
        project.setCustomer(customer);
        project.setLand(land);

        project.setBuildRequest(buildRequest);
        buildRequest.setProject(project);
        project.setBuildRequest(buildRequest);

        projectRepository.save(project);
        buildRequestRepository.save(buildRequest);
    }
    public void updateStatus(User authUser, Integer requestId, String status) {

        if (!authUser.getRole().equals("ADMIN")) {
            throw new ApiException("Only ADMIN can update build request status");
        }

        if (!status.equalsIgnoreCase("PROCESSING") && !status.equalsIgnoreCase("APPROVED") && !status.equalsIgnoreCase("REJECTED")) {
            throw new ApiException("Invalid status");
        }

        BuildRequest buildRequest =
                buildRequestRepository.getBuildRequestById(requestId);

        if (buildRequest == null) {
            throw new ApiException("Build request not found");
        }

        buildRequest.setStatus(status.toUpperCase());
        buildRequestRepository.save(buildRequest);
    }

    public void delete(User authUser, Integer requestId) {

        if (!authUser.getRole().equals("USER")) {
            throw new ApiException("Only customers can delete build requests");
        }

        Customer customer = customerRepository.getCustomerById(authUser.getId());

        BuildRequest buildRequest =
                buildRequestRepository.getBuildRequestById(requestId);

        if (buildRequest == null) {
            throw new ApiException("Build request not found");
        }

        if (!buildRequest.getCustomer().getId().equals(customer.getId())) {
            throw new ApiException("You are not authorized to delete this build request");
        }

        buildRequestRepository.delete(buildRequest);
    }

    ///Extra endpoints

    public BuildRequest getBuildRequestById(User authUser, Integer id) {

        BuildRequest buildRequest = buildRequestRepository.getBuildRequestById(id);

        if (buildRequest == null) {
            throw new ApiException("Build request not found");
        }

        if (authUser.getRole().equals("USER") && !buildRequest.getCustomer().getId().equals(authUser.getId())) {
            throw new ApiException("Unauthorized access");
        }

        return buildRequest;
    }

    public List<BuildRequest> getBuildRequestsByStatus(User authUser, String status) {

        if (!authUser.getRole().equals("ADMIN")) {
            throw new ApiException("Only ADMIN can view requests by status");
        }

        List<BuildRequest> buildRequests =
                buildRequestRepository.getBuildRequestsByStatus(status.toUpperCase());

        if (buildRequests.isEmpty()) {
            throw new ApiException("No build requests with status " + status + " found");
        }

        return buildRequests;
    }

    public List<BuildRequest> getMyBuildRequests(User authUser) {

        if (!authUser.getRole().equals("USER")) {
            throw new ApiException("Only customers can view their build requests");
        }

        List<BuildRequest> buildRequests = buildRequestRepository.getBuildRequestsByCustomerId(authUser.getId());

        if (buildRequests.isEmpty()) {
            throw new ApiException("No build requests found");
        }

        return buildRequests;
    }

    public List<BuildRequest> getBuildRequestsByLandId(User authUser, Integer landId) {

        Land land = landRepository.getLandById(landId);

        if (land == null) {
            throw new ApiException("Land not found");
        }

        if (authUser.getRole().equals("USER")
                && !land.getCustomer().getId().equals(authUser.getId())) {
            throw new ApiException("Unauthorized access");
        }

        List<BuildRequest> buildRequests = buildRequestRepository.getBuildRequestsByLandId(landId);

        if (buildRequests.isEmpty()) {
            throw new ApiException("No build requests found for this land");
        }

        return buildRequests;
    }

    public void approveRequest(User authUser, Integer requestId) {

        if (!authUser.getRole().equals("ADMIN")) {
            throw new ApiException("Only ADMIN can approve requests");
        }

        BuildRequest buildRequest = buildRequestRepository.getBuildRequestById(requestId);

        if (buildRequest == null) {
            throw new ApiException("Request not found");
        }

        Project project = buildRequest.getProject();
        Land land = buildRequest.getLand();

        if (project == null || land == null) {
            throw new ApiException("Project or land not found");
        }

        buildRequest.setStatus("APPROVED");
        project.setLand(land);
        land.setProject(project);

        projectRepository.save(project);
        landRepository.save(land);
        buildRequestRepository.save(buildRequest);
    }
}
