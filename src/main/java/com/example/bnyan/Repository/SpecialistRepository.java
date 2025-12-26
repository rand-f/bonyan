package com.example.bnyan.Repository;

import com.example.bnyan.Model.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialistRepository extends JpaRepository<Specialist,Integer> {

    Specialist findSpecialistById(Integer id);
}
