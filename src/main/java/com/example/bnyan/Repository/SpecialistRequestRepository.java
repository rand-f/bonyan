package com.example.bnyan.Repository;

import com.example.bnyan.Model.Specialist;
import com.example.bnyan.Model.SpecialistRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialistRequestRepository extends JpaRepository<SpecialistRequest,Integer> {
    SpecialistRequest findSpecialistRequestById(Integer id);
}

