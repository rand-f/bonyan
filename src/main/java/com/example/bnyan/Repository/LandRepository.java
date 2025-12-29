package com.example.bnyan.Repository;

import com.example.bnyan.Model.Land;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LandRepository extends JpaRepository<Land, Integer> {

    Land getLandById(Integer id);

    List<Land> getLandsByCustomerId(Integer customerId);


}