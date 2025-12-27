package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Built;
import com.example.bnyan.Model.Customer;
import com.example.bnyan.Model.UserRequest;
import com.example.bnyan.Repository.BuiltRepository;
import com.example.bnyan.Repository.CustomerRepository;
import com.example.bnyan.Repository.UserRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRequestService {

    private final UserRequestRepository userRequestRepository;
    private final CustomerRepository customerRepository;
    private final BuiltRepository builtRepository;

    public List<UserRequest> get() {
        List<UserRequest> userRequests = userRequestRepository.findAll();
        if (userRequests.isEmpty()) throw new ApiException("No user requests found");
        return userRequests;
    }

    public void add(Integer customerId, Integer builtId, UserRequest userRequest) {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) throw new ApiException("Customer not found");

        Built built = builtRepository.getBuiltById(builtId);
        if (built == null) throw new ApiException("Built not found");

        if (userRequest.getType().equalsIgnoreCase("buy") && !built.getStatus().equalsIgnoreCase("forSell"))
            throw new ApiException("This built is not for sale");

        if (userRequest.getType().equalsIgnoreCase("rent") && !built.getStatus().equalsIgnoreCase("forRent"))
            throw new ApiException("This built is not for rent");

        userRequest.setBuilt(built);
        userRequest.setCustomer(customer);
        userRequest.setStatus("pending");
        userRequest.setCreatedAt(LocalDateTime.now());
        userRequestRepository.save(userRequest);

        sendUserRequestNotification(customer, built, userRequest);
    }

    public void acceptRequest(Integer requestId) {
        UserRequest userRequest = userRequestRepository.getUserRequestById(requestId);
        if (userRequest == null) throw new ApiException("User request not found");

        if (!userRequest.getStatus().equalsIgnoreCase("pending"))
            throw new ApiException("Only pending requests can be accepted");

        userRequest.setStatus("accepted");
        userRequestRepository.save(userRequest);

        Built built = userRequest.getBuilt();
        if (userRequest.getType().equalsIgnoreCase("buy") || userRequest.getType().equalsIgnoreCase("rent")) {
            built.setStatus("owned");
            builtRepository.save(built);
        }

        sendAcceptNotification(userRequest);
    }

    public void rejectRequest(Integer requestId) {
        UserRequest userRequest = userRequestRepository.getUserRequestById(requestId);
        if (userRequest == null) throw new ApiException("User request not found");

        if (!userRequest.getStatus().equalsIgnoreCase("pending"))
            throw new ApiException("Only pending requests can be rejected");

        userRequest.setStatus("rejected");
        userRequestRepository.save(userRequest);

        sendRejectNotification(userRequest);
    }

    public void delete(Integer requestId, Integer customerId) {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) throw new ApiException("Customer not found");

        UserRequest userRequest = userRequestRepository.getUserRequestById(requestId);
        if (userRequest == null) throw new ApiException("User request not found");

        if (!userRequest.getCustomer().getId().equals(customerId))
            throw new ApiException("You are not authorized to delete this request");

        userRequestRepository.delete(userRequest);
    }

    public UserRequest getUserRequestById(Integer id) {
        UserRequest userRequest = userRequestRepository.getUserRequestById(id);
        if (userRequest == null) throw new ApiException("User request not found");
        return userRequest;
    }

    public List<UserRequest> getUserRequestsByStatus(String status) {
        List<UserRequest> userRequests = userRequestRepository.getUserRequestsByStatus(status);
        if (userRequests.isEmpty()) throw new ApiException("No user requests with status " + status + " found");
        return userRequests;
    }

    public List<UserRequest> getUserRequestsByType(String type) {
        List<UserRequest> userRequests = userRequestRepository.getUserRequestsByType(type);
        if (userRequests.isEmpty()) throw new ApiException("No user requests with type " + type + " found");
        return userRequests;
    }

    public List<UserRequest> getUserRequestsByCustomerId(Integer customerId) {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) throw new ApiException("Customer not found");

        List<UserRequest> userRequests = userRequestRepository.getUserRequestsByCustomerId(customerId);
        if (userRequests.isEmpty()) throw new ApiException("No user requests found for this customer");
        return userRequests;
    }

    // n8n Webhooks
    private void sendUserRequestNotification(Customer customer, Built built, UserRequest userRequest) {
        String webhookUrl = "http://localhost:5678/webhook/user-request-created";

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("requestId", userRequest.getId());
        payload.put("requestType", userRequest.getType());
        payload.put("requestStatus", userRequest.getStatus());
        payload.put("buyerName", customer.getUser().getUsername());
        payload.put("buyerEmail", customer.getUser().getEmail());
        payload.put("buyerPhone", customer.getUser().getPhoneNumber());
        payload.put("propertyLocation", built.getLocation());
        payload.put("propertyDescription", built.getDescription());
        payload.put("propertySize", built.getSize());
        payload.put("propertyPrice", built.getPrice());
        payload.put("propertyStatus", built.getStatus());
        payload.put("sellerName", built.getUser().getUsername());
        payload.put("sellerEmail", built.getUser().getEmail());
        payload.put("sellerPhone", built.getUser().getPhoneNumber());
        payload.put("createdAt", userRequest.getCreatedAt().toString());

        try {
            new RestTemplate().postForObject(webhookUrl, payload, String.class);
        } catch (Exception e) {
            System.out.println("Failed to send webhook: " + e.getMessage());
        }
    }

    private void sendAcceptNotification(UserRequest userRequest) {
        String webhookUrl = "http://localhost:5678/webhook/user-request-accepted";

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("requestId", userRequest.getId());
        payload.put("requestType", userRequest.getType());
        payload.put("buyerName", userRequest.getCustomer().getUser().getUsername());
        payload.put("buyerEmail", userRequest.getCustomer().getUser().getEmail());
        payload.put("buyerPhone", userRequest.getCustomer().getUser().getPhoneNumber());
        payload.put("propertyLocation", userRequest.getBuilt().getLocation());
        payload.put("propertyDescription", userRequest.getBuilt().getDescription());
        payload.put("propertyPrice", userRequest.getBuilt().getPrice());
        payload.put("sellerName", userRequest.getBuilt().getUser().getUsername());
        payload.put("sellerEmail", userRequest.getBuilt().getUser().getEmail());
        payload.put("sellerPhone", userRequest.getBuilt().getUser().getPhoneNumber());
        payload.put("acceptedAt", LocalDateTime.now().toString());

        try {
            new RestTemplate().postForObject(webhookUrl, payload, String.class);
        } catch (Exception e) {
            System.out.println("Failed to send webhook: " + e.getMessage());
        }
    }

    private void sendRejectNotification(UserRequest userRequest) {
        String webhookUrl = "http://localhost:5678/webhook/user-request-rejected";

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("requestId", userRequest.getId());
        payload.put("requestType", userRequest.getType());
        payload.put("buyerName", userRequest.getCustomer().getUser().getUsername());
        payload.put("buyerEmail", userRequest.getCustomer().getUser().getEmail());
        payload.put("buyerPhone", userRequest.getCustomer().getUser().getPhoneNumber());
        payload.put("propertyLocation", userRequest.getBuilt().getLocation());
        payload.put("propertyDescription", userRequest.getBuilt().getDescription());
        payload.put("propertyPrice", userRequest.getBuilt().getPrice());
        payload.put("sellerName", userRequest.getBuilt().getUser().getUsername());
        payload.put("sellerEmail", userRequest.getBuilt().getUser().getEmail());
        payload.put("sellerPhone", userRequest.getBuilt().getUser().getPhoneNumber());
        payload.put("rejectedAt", LocalDateTime.now().toString());

        try {
            new RestTemplate().postForObject(webhookUrl, payload, String.class);
        } catch (Exception e) {
            System.out.println("Failed to send webhook: " + e.getMessage());
        }
    }
}