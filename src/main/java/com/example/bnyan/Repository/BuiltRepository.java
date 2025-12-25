package com.example.bnyan.Repository;

import com.example.bnyan.Model.Built;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuiltRepository extends JpaRepository<Built, Integer> {
    Built getBuiltById(Integer id);

    @Query("select b from Built b where b.status = ?1")
    List<Built> getBuiltsByStatus(String status);

    @Query("select b from Built b where b.price <= ?1 order by b.price asc")
    List<Built> getBuiltsByPriceLessThanOrEqual(Double price);

    @Query("select b from Built b where b.location like %?1%")
    List<Built> getBuiltsByLocation(String location);

    @Query("select b from Built b where b.user.id = ?1")
    List<Built> getBuiltsByUserId(Integer userId);
}