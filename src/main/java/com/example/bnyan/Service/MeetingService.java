package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Meeting;
import com.example.bnyan.Model.Project;
import com.example.bnyan.Repository.MeetingRepository;
import com.example.bnyan.Repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final ProjectRepository projectRepository;

    public List<Meeting> getAll() {
        return meetingRepository.findAll();
    }

    public List<Meeting> getByProject(Integer projectId) {
        Project project = projectRepository.findProjectById(projectId);
        if (project == null) {
            throw new ApiException("project not found");
        }
        return meetingRepository.findAllByProjectId(projectId);
    }

    public void addMeeting(Integer projectId, Meeting meeting) {
        Project project = projectRepository.findProjectById(projectId);
        if (project == null) {
            throw new ApiException("project not found");
        }

        meeting.setProject(project);
        meeting.setCreatedAt(LocalDateTime.now());
        meetingRepository.save(meeting);
    }


    public void deleteMeeting(Integer id) {
        Meeting meeting = meetingRepository.findMeetingById(id);
        if (meeting == null) {
            throw new ApiException("meeting not found");
        }
        meetingRepository.delete(meeting);
    }
}
