package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Customer;
import com.example.bnyan.Model.Land;
import com.example.bnyan.Model.User;
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

    /// crud

    public List<Land> getAllLands(User authUser) {

        if (!authUser.getRole().equals("ADMIN")) {
            throw new ApiException("Only ADMIN can view all lands");
        }

        List<Land> lands = landRepository.findAll();

        if (lands.isEmpty()) {
            throw new ApiException("No lands found");
        }

        return lands;
    }

    public void add(User authUser, Land land) {

        if (!authUser.getRole().equals("USER")) {
            throw new ApiException("Only customers can add lands");
        }

        Customer customer = customerRepository.getCustomerById(authUser.getId());
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        land.setCustomer(customer);
        land.setCreatedAt(LocalDateTime.now());
        land.setAuthorizationStatus(false);

        landRepository.save(land);
    }

    public void update(User authUser, Integer landId, Land newLand) {

        if (!authUser.getRole().equals("USER")) {
            throw new ApiException("Only customers can update lands");
        }

        Land land = landRepository.getLandById(landId);
        if (land == null) {
            throw new ApiException("Land not found");
        }

        if (!land.getCustomer().getId().equals(authUser.getId())) {
            throw new ApiException("Unauthorized");
        }

        land.setLocation(newLand.getLocation());
        land.setSize(newLand.getSize());
        land.setAuthorizationStatus(newLand.getAuthorizationStatus());

        landRepository.save(land);
    }

    public void delete(User authUser, Integer landId) {

        if (!authUser.getRole().equals("USER")) {
            throw new ApiException("Only customers can delete lands");
        }

        Land land = landRepository.getLandById(landId);
        if (land == null) {
            throw new ApiException("Land not found");
        }

        if (!land.getCustomer().getId().equals(authUser.getId())) {
            throw new ApiException("Unauthorized");
        }

        landRepository.delete(land);
    }

    /// extra endpoints

    public List<Land> getMyLands(User authUser) {

        if (!authUser.getRole().equals("USER")) {
            throw new ApiException("Only customers can view their lands");
        }

        List<Land> lands = landRepository.getLandsByCustomerId(authUser.getId());
        if (lands.isEmpty()) {
            throw new ApiException("No lands found for this customer");
        }

        return lands;
    }

    public Land getLandById(User authUser, Integer landId) {

        Land land = landRepository.getLandById(landId);
        if (land == null) {
            throw new ApiException("Land not found");
        }

        if (authUser.getRole().equals("USER") && !land.getCustomer().getId().equals(authUser.getId())) {
            throw new ApiException("Unauthorized access");
        }

        return land;
    }
}


