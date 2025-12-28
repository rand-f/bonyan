package com.example.bnyan.Repository;

import com.example.bnyan.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User getUserById(Integer id);

    User getUserByUsername(String username);

    User getUserByEmail(String email);

    User findUserByUsername(String username);

    @Query("select u from User u where u.role = ?1")
    List<User> getUsersByRole(String role);

    @Query("select u from User u where u.phoneNumber = ?1")
    User getUserByPhoneNumber(String phoneNumber);
}