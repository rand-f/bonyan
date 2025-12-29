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

    ///  Crud

    public List<Land> getAllLands() {

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

    public void update(Integer landId, Integer customerId, Land newLand) {

        Land land = landRepository.getLandById(landId);
        if (land == null) {
            throw new ApiException("Land not found");
        }

        if (!land.getCustomer().getId().equals(customerId)) {
            throw new ApiException("Unauthorized");
        }

        land.setLocation(newLand.getLocation());
        land.setSize(newLand.getSize());
        land.setAuthorizationStatus(newLand.getAuthorizationStatus());

        landRepository.save(land);
    }



    public void delete(Integer landId, Integer customerId) {

        Land land = landRepository.getLandById(landId);
        if (land == null) {
            throw new ApiException("Land not found");
        }

        if (!land.getCustomer().getId().equals(customerId)) {
            throw new ApiException("Unauthorized");
        }

        landRepository.delete(land);
    }


    ///  extra endpoints

    // Lands owned by a customer (Figma: My Lands)
    public List<Land> getLandsByCustomer(Integer customerId) {

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

    public Land getLandById(Integer landId) {

        Land land = landRepository.getLandById(landId);
        if (land == null) {
            throw new ApiException("Land not found");
        }

        return land;
    }

}

