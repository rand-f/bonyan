package com.example.bnyan.Repository;

import com.example.bnyan.Model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {

    Meeting findMeetingById(Integer id);

    List<Meeting> findAllByProjectId(Integer projectId);
}
