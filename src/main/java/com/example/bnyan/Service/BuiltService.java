package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Built;
import com.example.bnyan.Model.User;
import com.example.bnyan.Repository.BuiltRepository;
import com.example.bnyan.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BuiltService {

    private final BuiltRepository builtRepository;
    private final UserRepository userRepository;

    public List<Built> get() {
        List<Built> builts = builtRepository.findAll();
        if (builts.isEmpty()) throw new ApiException("No built found");
        return builts;
    }

    public void add(Integer userId, Built built) {
        User user = userRepository.getUserById(userId);
        if (user == null) throw new ApiException("User not found");

        built.setUser(user);
        built.setCreatedAt(LocalDateTime.now());
        builtRepository.save(built);
    }

    public void update(Integer builtId, Integer userId, Built built) {
        User user = userRepository.getUserById(userId);
        if (user == null) throw new ApiException("User not found");

        Built old = builtRepository.getBuiltById(builtId);
        if (old == null) throw new ApiException("Built not found");

        if (!old.getUser().getId().equals(userId))
            throw new ApiException("You are not authorized to update this built");

        old.setLocation(built.getLocation());
        old.setDescription(built.getDescription());
        old.setSize(built.getSize());
        old.setPrice(built.getPrice());
        old.setStatus(built.getStatus());

        builtRepository.save(old);
    }

    public void delete(Integer builtId, Integer userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) throw new ApiException("User not found");

        Built built = builtRepository.getBuiltById(builtId);
        if (built == null) throw new ApiException("Built not found");

        if (!built.getUser().getId().equals(userId))
            throw new ApiException("You are not authorized to delete this built");

        builtRepository.delete(built);
    }

    public Built getBuiltById(Integer id) {
        Built built = builtRepository.getBuiltById(id);
        if (built == null) throw new ApiException("Built not found");
        return built;
    }

    public List<Built> getBuiltsByStatus(String status) {
        List<Built> builts = builtRepository.getBuiltsByStatus(status);
        if (builts.isEmpty()) throw new ApiException("No builts with status " + status + " found");
        return builts;
    }

    public List<Built> getBuiltsByPriceLessThanOrEqual(Double price) {
        List<Built> builts = builtRepository.getBuiltsByPriceLessThanOrEqual(price);
        if (builts.isEmpty()) throw new ApiException("No builts found with price less than or equal to " + price);
        return builts;
    }

    public List<Built> getBuiltsByLocation(String location) {
        List<Built> builts = builtRepository.getBuiltsByLocation(location);
        if (builts.isEmpty()) throw new ApiException("No builts found in location " + location);
        return builts;
    }

    public List<Built> getBuiltsByUserId(Integer userId) {
        User user = userRepository.getUserById(userId);
        if (user == null) throw new ApiException("User not found");

        List<Built> builts = builtRepository.getBuiltsByUserId(userId);
        if (builts.isEmpty()) throw new ApiException("No builts found for this user");
        return builts;
    }
}