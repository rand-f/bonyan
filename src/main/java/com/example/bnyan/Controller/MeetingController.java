package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.Model.Meeting;
import com.example.bnyan.Model.User;
import com.example.bnyan.Service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/meeting")
@RequiredArgsConstructor
public class MeetingController {

    private final MeetingService meetingService;

    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(meetingService.getAll());
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getByProject(@PathVariable Integer projectId) {
        return ResponseEntity.ok(meetingService.getByProject(projectId));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMeeting(@AuthenticationPrincipal User user, @RequestBody Meeting meeting) {
        meetingService.addMeeting(user.getId(), meeting);
        return ResponseEntity.ok(new ApiResponse("Meeting added successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMeeting(@PathVariable Integer id) {
        meetingService.deleteMeeting(id);
        return ResponseEntity.ok(new ApiResponse("Meeting deleted successfully"));
    }
}
