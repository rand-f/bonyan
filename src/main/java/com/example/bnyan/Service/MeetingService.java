package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Meeting;
import com.example.bnyan.Model.Project;
import com.example.bnyan.Model.Specialist;
import com.example.bnyan.Repository.MeetingRepository;
import com.example.bnyan.Repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // 1. Create Zoom meeting
        String zoomLink = createZoomMeeting(meeting);

        // 2. Save meeting
        meeting.setLink(zoomLink);
        meeting.setProject(project);
        meeting.setCreatedAt(LocalDateTime.now());
        meetingRepository.save(meeting);

        // 3. Notify each specialist (old webhook style)
        for (Specialist specialist : project.getSpecialists()) {

            sendMeetingEmail(
                    specialist.getUser().getUsername(),
                    specialist.getUser().getEmail(),
                    meeting
            );
        }
    }

    public void deleteMeeting(Integer id) {
        Meeting meeting = meetingRepository.findMeetingById(id);
        if (meeting == null) {
            throw new ApiException("meeting not found");
        }
        meetingRepository.delete(meeting);
    }
    private String createZoomMeeting(Meeting meeting) {

        String webhookUrl = "http://localhost:5678/webhook/zoom-meeting";

        HashMap<String, Object> payload = new HashMap<>();
        payload.put("topic", meeting.getTopic());
        payload.put("startDate", meeting.getStartDate().toString());
        payload.put("duration", meeting.getDuration());

        try {
            RestTemplate restTemplate = new RestTemplate();

            Map response = restTemplate.postForObject(
                    webhookUrl,
                    payload,
                    Map.class
            );

            return response.get("link").toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create Zoom meeting");
        }
    }
    private void sendMeetingEmail(
            String receiverName,
            String receiverEmail,
            Meeting meeting
    ) {

        String webhookUrl = "http://localhost:5678/webhook/meeting-email";

        HashMap<String, Object> payload = new HashMap<>();

        payload.put("receiverName", receiverName);
        payload.put("receiverEmail", receiverEmail);


        payload.put("startDate", meeting.getStartDate().toString());
        payload.put("duration", meeting.getDuration());
        payload.put("meetingLink", meeting.getLink());

        payload.put("sentAt", LocalDateTime.now().toString());

        try {
            new RestTemplate().postForObject(webhookUrl, payload, String.class);
        } catch (Exception e) {
            System.out.println("Failed to send meeting email to " + receiverEmail);
        }
    }


}
