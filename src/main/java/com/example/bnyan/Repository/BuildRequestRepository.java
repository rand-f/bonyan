package com.example.bnyan.Repository;

import com.example.bnyan.Model.BuildRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildRequestRepository extends JpaRepository<BuildRequest, Integer> {

    BuildRequest getBuildRequestById(Integer id);

    BuildRequest getBuildRequestByLandId(Integer landId);

    List<BuildRequest> getBuildRequestsByStatus(String status);

    List<BuildRequest> getBuildRequestsByCustomerId(Integer customerId);

    List<BuildRequest> getBuildRequestsByLandId(Integer landId);

    BuildRequest getBuildRequestByProjectId(Integer projectId);

}