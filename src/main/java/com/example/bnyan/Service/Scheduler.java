package com.example.bnyan.Service;

import com.example.bnyan.Model.Project;
import com.example.bnyan.Model.SpecialistRequest;
import com.example.bnyan.Repository.ProjectRepository;
import com.example.bnyan.Repository.SpecialistRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Scheduler {

    private final ProjectRepository projectRepository;
    private final SpecialistRequestRepository specialistRequestRepository;

    @Scheduled(fixedRate = 60000) // every 1 minute
    public void startProject(){
        List<Project>projects=projectRepository.findProjectsByStatusAndStartDateBefore("preparing", LocalDate.now());

        for (int i=0;i<projects.size();i++){
            Project project = projects.get(i);
            project.setStatus("on going");
            projectRepository.save(project);
        }
    }

    @Scheduled(fixedRate = 60000) // every 1 minute
    public void autoReject(){
        List<SpecialistRequest> specialistRequests= specialistRequestRepository.getSpecialistRequestByStatus("pending");

        for (SpecialistRequest request:specialistRequests){
            if(request.getCreated_at().isBefore(LocalDateTime.now().plusDays(7))){
                request.setStatus("rejected");
            }
        }
    }

}
