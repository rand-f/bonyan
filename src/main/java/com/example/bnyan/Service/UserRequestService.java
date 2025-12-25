package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Built;
import com.example.bnyan.Model.Customer;
import com.example.bnyan.Model.User;
import com.example.bnyan.Model.UserRequest;
import com.example.bnyan.Repository.BuiltRepository;
import com.example.bnyan.Repository.CustomerRepository;
import com.example.bnyan.Repository.UserRepository;
import com.example.bnyan.Repository.UserRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRequestService {

    private final UserRequestRepository userRequestRepository;
    private final UserRepository userRepository;
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
    }

    public void updateStatus(Integer requestId, Integer adminId, String status) {
        User admin = userRepository.getUserById(adminId);
        if (admin == null) throw new ApiException("User not found");

        if (!admin.getRole().equalsIgnoreCase("ADMIN"))
            throw new ApiException("Only ADMIN can update request status");

        UserRequest userRequest = userRequestRepository.getUserRequestById(requestId);
        if (userRequest == null) throw new ApiException("User request not found");

        userRequest.setStatus(status);
        userRequestRepository.save(userRequest);

        if (status.equalsIgnoreCase("accepted")) {
            Built built = userRequest.getBuilt();
            if (userRequest.getType().equalsIgnoreCase("buy") || userRequest.getType().equalsIgnoreCase("rent")) {
                built.setStatus("owned");
                builtRepository.save(built);
            }
        }
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
}