package com.example.bnyan.Repository;

import com.example.bnyan.Model.Specialist;
import com.example.bnyan.Model.SpecialistRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SpecialistRequestRepository extends JpaRepository<SpecialistRequest,Integer> {
    SpecialistRequest findSpecialistRequestById(Integer id);

    List<SpecialistRequest> findSpecialistRequestBySpecialist(Specialist specialist);

    @Query("select r from SpecialistRequest r where r.status=?1")
    List<SpecialistRequest> getSpecialistRequestByStatus(String status);
}


