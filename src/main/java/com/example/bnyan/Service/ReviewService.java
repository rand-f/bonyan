package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Customer;
import com.example.bnyan.Model.Project;
import com.example.bnyan.Model.Review;
import com.example.bnyan.Model.Specialist;
import com.example.bnyan.Repository.CustomerRepository;
import com.example.bnyan.Repository.ProjectRepository;
import com.example.bnyan.Repository.ReviewRepository;
import com.example.bnyan.Repository.SpecialistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final SpecialistRepository specialistRepository;
    private final ProjectRepository projectRepository;

    public List<Review> get() {
        List<Review> reviews = reviewRepository.findAll();
        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found");
        }
        return reviews;
    }

    public void add(Integer customerId, Review review, Integer spec_id) {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        Specialist specialist = specialistRepository.findSpecialistById(spec_id);
        if (specialist == null) {
            throw new ApiException("Specialist not found");
        }

        Boolean workedWith = false;

        List<Project>myProjects=projectRepository.findProjectsByCustomer(customer);
        for(Project project:myProjects){
            for (Specialist special:project.getSpecialists()){
                if(special==specialist){
                    workedWith=true;
                }
            }
        }

        if(!workedWith){
            throw new ApiException("you can not review a specialist you did not work with");
        }

        review.setSpecialist(specialist);
        review.setCustomer(customer);
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);
    }

    public void delete(Integer reviewId, Integer customerId) {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        Review review = reviewRepository.getReviewById(reviewId);
        if (review == null) {
            throw new ApiException("Review not found");
        }
        if (!review.getCustomer().getId().equals(customerId)) {
            throw new ApiException("You are not authorized to delete this review");
        }
        reviewRepository.delete(review);
    }

    ///  extra endpoints

    public Review getReviewById(Integer id) {
        Review review = reviewRepository.getReviewById(id);
        if (review == null) {
            throw new ApiException("Review not found");
        }
        return review;
    }

    public List<Review>getSpecialistReviews(Integer spec_id){
        Specialist specialist = specialistRepository.findSpecialistById(spec_id);
        if (specialist == null) {
            throw new ApiException("Specialist not found");
        }
        return reviewRepository.findReviewsBySpecialist(specialist);
    }

    public List<Review>getReviewsByCustomer(Integer customer_id){
        Customer customer = customerRepository.getCustomerById(customer_id);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        return reviewRepository.findReviewsByCustomer(customer);
    }

}
