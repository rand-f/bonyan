package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Customer;
import com.example.bnyan.Model.Land;
import com.example.bnyan.Repository.CustomerRepository;
import com.example.bnyan.Repository.LandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LandService {

    private final LandRepository landRepository;
    private final CustomerRepository customerRepository;

    public List<Land> get() {
        List<Land> lands = landRepository.findAll();
        if (lands.isEmpty()) {
            throw new ApiException("No lands found");
        }
        return lands;
    }

    public void add(Integer customerId, Land land) {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        land.setCustomer(customer);
        land.setCreatedAt(LocalDateTime.now());
        land.setAuthorizationStatus(false);
        landRepository.save(land);
    }

    public void update(Integer landId, Integer customerId, Land land) {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        Land oldLand = landRepository.getLandById(landId);
        if (oldLand == null) {
            throw new ApiException("Land not found");
        }
        if (!oldLand.getCustomer().getId().equals(customerId)) {
            throw new ApiException("You are not authorized to update this land");
        }
        oldLand.setLocation(land.getLocation());
        oldLand.setSize(land.getSize());
        oldLand.setAuthorizationStatus(land.getAuthorizationStatus());

        landRepository.save(oldLand);
    }

    public void delete(Integer landId, Integer customerId) {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        Land land = landRepository.getLandById(landId);
        if (land == null) {
            throw new ApiException("Land not found");
        }
        if (!land.getCustomer().getId().equals(customerId)) {
            throw new ApiException("You are not authorized to delete this land");
        }
        landRepository.delete(land);
    }

    ///  extra endpoints

    public Land getLandById(Integer id) {
        Land land = landRepository.getLandById(id);
        if (land == null) {
            throw new ApiException("Land not found");
        }
        return land;
    }

    public List<Land> getLandsByCustomerId(Integer customerId) {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        List<Land> lands = landRepository.getLandsByCustomerId(customerId);
        if (lands.isEmpty()) {
            throw new ApiException("No lands found for this customer");
        }
        return lands;
    }
}
