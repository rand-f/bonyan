package com.example.bnyan.Repository;

import com.example.bnyan.Model.Customer;
import com.example.bnyan.Model.Review;
import com.example.bnyan.Model.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Review getReviewById(Integer id);

    List<Review> findReviewsBySpecialist(Specialist specialist);

    List<Review> findReviewsByCustomer(Customer customer);
    Review getReviewByCustomerIdAndSpecialistId(Integer customerId, Integer specialistId);

    List<Review> getReviewsBySpecialistId(Integer specialistId);

    List<Review> getReviewsByCustomerId(Integer customerId);

}

