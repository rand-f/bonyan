package com.example.bnyan.Repository;

import com.example.bnyan.Model.Domain;
import com.example.bnyan.Model.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface DomainRepository extends JpaRepository<Domain,Integer> {
    Domain findDomainById(Integer id);

    Domain findDomainBySpecialists(Set<Specialist> specialists);
    Domain findBySpecialists_Id(Integer specialistId);

}


