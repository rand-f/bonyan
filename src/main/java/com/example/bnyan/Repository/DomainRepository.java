package com.example.bnyan.Repository;

import com.example.bnyan.Model.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainRepository extends JpaRepository<Domain,Integer> {
    Domain findDomainById(Integer id);
}

