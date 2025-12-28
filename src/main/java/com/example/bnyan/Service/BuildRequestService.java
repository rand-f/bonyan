package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
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
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final LandRepository landRepository;
    private final ProjectRepository projectRepository;

    public List<BuildRequest> get() {
        List<BuildRequest> buildRequests = buildRequestRepository.findAll();
        if (buildRequests.isEmpty()) {
            throw new ApiException("No build requests found");
        }
        return buildRequests;
    }

    public void add(Integer customerId, Integer landId, BuildRequest buildRequest) {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        Land land = landRepository.getLandById(landId);
        if (land == null) {
            throw new ApiException("Land not found");
        }

        buildRequest.setCustomer(customer);
        buildRequest.setLand(land);
        buildRequest.setStatus("processing");
        buildRequest.setCreatedAt(LocalDateTime.now());

        buildRequestRepository.save(buildRequest);
    }

    public void updateStatus(Integer requestId, Integer adminId, String status) {
        User admin = userRepository.getUserById(adminId);
        if (admin == null) {
            throw new ApiException("User not found");
        }

        if (!admin.getRole().equalsIgnoreCase("ADMIN")) {
            throw new ApiException("Only ADMIN can update build request status");
        }

        BuildRequest buildRequest = buildRequestRepository.getBuildRequestById(requestId);
        if (buildRequest == null) {
            throw new ApiException("Build request not found");
        }

        buildRequest.setStatus(status);
        buildRequestRepository.save(buildRequest);
    }

    public void delete(Integer requestId, Integer customerId) {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        BuildRequest buildRequest = buildRequestRepository.getBuildRequestById(requestId);
        if (buildRequest == null) {
            throw new ApiException("Build request not found");
        }

        if (!buildRequest.getCustomer().getId().equals(customerId)) {
            throw new ApiException("You are not authorized to delete this build request");
        }

        buildRequestRepository.delete(buildRequest);
    }

    ///  extra end points :

    public BuildRequest getBuildRequestById(Integer id) {
        BuildRequest buildRequest = buildRequestRepository.getBuildRequestById(id);
        if (buildRequest == null) {
            throw new ApiException("Build request not found");
        }

        return buildRequest;
    }

    public List<BuildRequest> getBuildRequestsByStatus(String status) {
        List<BuildRequest> buildRequests = buildRequestRepository.getBuildRequestsByStatus(status);
        if (buildRequests.isEmpty()) {
            throw new ApiException("No build requests with status " + status + " found");
        }

        return buildRequests;
    }

    public List<BuildRequest> getBuildRequestsByCustomerId(Integer customerId) {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        List<BuildRequest> buildRequests = buildRequestRepository.getBuildRequestsByCustomerId(customerId);
        if (buildRequests.isEmpty()) {
            throw new ApiException("No build requests found for this customer");
        }
        return buildRequests;
    }

    public List<BuildRequest> getBuildRequestsByLandId(Integer landId) {
        Land land = landRepository.getLandById(landId);
        if (land == null) {
            throw new ApiException("Land not found");
        }
        List<BuildRequest> buildRequests = buildRequestRepository.getBuildRequestsByLandId(landId);

        if (buildRequests.isEmpty()) {
            throw new ApiException("No build requests found for this land");
        }

        return buildRequests;
    }

    public void approveRequest(//Integer using_id,
                               Integer request_id){

        //authorization can be done later

//        User user = userRepository.getUserById(using_id);
//        if (user==null){
//            throw new ApiException("user not found");
//        }
//        if(!user.getRole().equals("ADMIN")){
//            throw new ApiException("unauthorized to make this gange");
//        }

        BuildRequest buildRequest= buildRequestRepository.getBuildRequestById(request_id);
        if(buildRequest==null){
            throw new ApiException("request not found");
        }

        Project project = projectRepository.findProjectById(buildRequest.getProject().getId());
        Land land = landRepository.getLandById(buildRequest.getLand().getId());

        if(project==null||land==null){
            throw new ApiException("can not complete this process because the project or land not found");
        }

        buildRequest.setStatus("approved");
        project.setLand(land);
        land.setProject(project);

        projectRepository.save(project);
        landRepository.save(land);
        buildRequestRepository.save(buildRequest);
    }

}
