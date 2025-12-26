package com.example.bnyan.Service;
import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Project;
import com.example.bnyan.Model.Specialist;
import com.example.bnyan.Model.SpecialistRequest;
import com.example.bnyan.Repository.ProjectRepository;
import com.example.bnyan.Repository.SpecialistRepository;
import com.example.bnyan.Repository.SpecialistRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialistRequestService {

    private final SpecialistRequestRepository requestRepository;
    private final SpecialistRepository specialistRepository;
    private final ProjectRepository projectRepository;

    public List<SpecialistRequest> getAll() {
        return requestRepository.findAll();
    }

    public void addRequest(SpecialistRequest request, Integer project_id,Integer spec_id) {
        Project project = projectRepository.findProjectById(project_id);
        Specialist specialist= specialistRepository.findSpecialistById(spec_id);

        if(project==null || specialist==null){
            throw new ApiException("this project request can nt be assigned to the specialist");
        }

        request.setProject(project);
        project.getRequests().add(request);
        request.setSpecialist(specialist);
        specialist.getRequests().add(request);

        projectRepository.save(project);
        specialistRepository.save(specialist);
        request.setStatus("pending");
        requestRepository.save(request);
    }

    public void updateRequest(Integer id, SpecialistRequest request) {
        SpecialistRequest old = requestRepository.findSpecialistRequestById(id);
        if (old == null) {
            throw new ApiException("request not found");
        }

        old.setDescription(request.getDescription());
        old.setOfferedPrice(request.getOfferedPrice());
        old.setStatus(request.getStatus());
        old.setExpectedStartDate(request.getExpectedStartDate());
        old.setProjectExpectedEndDate(request.getProjectExpectedEndDate());

        requestRepository.save(old);
    }

    public void deleteRequest(Integer id) {
        SpecialistRequest request = requestRepository.findSpecialistRequestById(id);
        if (request == null) {
            throw new ApiException("request not found");
        }
        requestRepository.delete(request);
    }


}
