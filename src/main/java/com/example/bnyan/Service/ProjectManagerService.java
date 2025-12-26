package com.example.bnyan.Service;
import com.example.bnyan.Api.ApiException;
import com.example.bnyan.DTO.CustomerDTO;
import com.example.bnyan.DTO.ProjectManagerDTO;
import com.example.bnyan.Model.Customer;
import com.example.bnyan.Model.ProjectManager;
import com.example.bnyan.Model.User;
import com.example.bnyan.Repository.ProjectManagerRepository;
import com.example.bnyan.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectManagerService {

    private final ProjectManagerRepository projectManagerRepository;
    private final UserRepository userRepository;

    public List<ProjectManager> getAll() {
        return projectManagerRepository.findAll();
    }

//    public void addProjectManager(ProjectManager manager) {
//        projectManagerRepository.save(manager);
//    }

    public void registerManager(ProjectManagerDTO managerDTO) {
        if (userRepository.getUserByUsername(managerDTO.getUsername()) != null)
            throw new ApiException("Username already exists");

        if (userRepository.getUserByEmail(managerDTO.getEmail()) != null)
            throw new ApiException("Email already exists");

        User user = new User();
        user.setUsername(managerDTO.getUsername());
        user.setPassword(managerDTO.getPassword());
        user.setEmail(managerDTO.getEmail());
        user.setPhoneNumber(managerDTO.getPhoneNumber());
        user.setRole("SPECIALIST");
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        ProjectManager projectManager =new ProjectManager();
        projectManager.setUser(user);
        projectManagerRepository.save(projectManager);
    }

    public void deleteProjectManager(Integer id) {
        ProjectManager manager = projectManagerRepository.findProjectManagerById(id);
        if (manager == null) {
            throw new ApiException("project manager not found");
        }
        projectManagerRepository.delete(manager);
    }
}
