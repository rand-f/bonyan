package com.example.bnyan.Repository;

import com.example.bnyan.Model.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, Integer> {
    UserRequest getUserRequestById(Integer id);

    @Query("select ur from UserRequest ur where ur.status = ?1")
    List<UserRequest> getUserRequestsByStatus(String status);

    @Query("select ur from UserRequest ur where ur.type = ?1")
    List<UserRequest> getUserRequestsByType(String type);

    @Query("select ur from UserRequest ur where ur.customer.id = ?1")
    List<UserRequest> getUserRequestsByCustomerId(Integer customerId);

    @Query("select ur from UserRequest ur where ur.built.id = ?1")
    List<UserRequest> getUserRequestsByBuiltId(Integer builtId);
}