package com.example.bnyan.Service;
import com.example.bnyan.Api.ApiException;
import com.example.bnyan.DTO.ProjectManagerDTO;
import com.example.bnyan.Model.*;
import com.example.bnyan.Repository.ProjectManagerRepository;
import com.example.bnyan.Repository.SpecialistRequestRepository;
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
    private final SpecialistRequestRepository specialistRequestRepository;

    public List<ProjectManager> getAll() {
        return projectManagerRepository.findAll();
    }


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

    public void exceptRequest(Integer manager_id, Integer request_id){
        ProjectManager manager= projectManagerRepository.findProjectManagerById(manager_id);
        SpecialistRequest specialistRequest =specialistRequestRepository.findSpecialistRequestById(request_id);

        if (manager==null||specialistRequest==null){
            throw new ApiException("can not except this request");
        }

        if(manager.getId()!=specialistRequest.getManager().getId()){
            throw new ApiException("unauthorized to except this request");
        }

        Project project =specialistRequest.getProject();

        manager.getProject().add(project);
        project.setManager(manager);

        specialistRequest.setStatus("excepted");
    }

    public void rejectRequest(Integer manager_id, Integer request_id){
        ProjectManager manager = projectManagerRepository.findProjectManagerById(manager_id);
        SpecialistRequest specialistRequest =specialistRequestRepository.findSpecialistRequestById(request_id);

        if (manager==null||specialistRequest==null){
            throw new ApiException("can not except this request");
        }

        if(manager.getId()!=specialistRequest.getSpecialist().getId()){
            throw new ApiException("unauthorized to except this request");
        }

        specialistRequest.setStatus("rejected");
    }

    public void deleteProjectManager(Integer id) {
        ProjectManager manager = projectManagerRepository.findProjectManagerById(id);
        if (manager == null) {
            throw new ApiException("project manager not found");
        }
        projectManagerRepository.delete(manager);
    }
}
