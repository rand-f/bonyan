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

import java.util.List;
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CustomerRepository customerRepository;
    private final SpecialistRepository specialistRepository;
    private final ProjectRepository projectRepository;

    ///  Crud

    public List<Review> getAllReviews() {

        List<Review> reviews = reviewRepository.findAll();
        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found");
        }

        return reviews;
    }

    public void add(Integer customerId, Integer specialistId, Review review) {

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
        Review existing =
                reviewRepository.getReviewByCustomerIdAndSpecialistId(customerId, specialistId);

        if (existing != null) {
            throw new ApiException("You already reviewed this specialist");
        }

        review.setCustomer(customer);
        review.setSpecialist(specialist);

        reviewRepository.save(review);
    }

    public void update(Integer reviewId, Review newReview) {

        Review review = reviewRepository.getReviewById(reviewId);
        if (review == null) {
            throw new ApiException("Review not found");
        }

        review.setRate(newReview.getRate());
        review.setComment(newReview.getComment());

        reviewRepository.save(review);
    }

    public void delete(Integer reviewId) {

        Review review = reviewRepository.getReviewById(reviewId);
        if (review == null) {
            throw new ApiException("Review not found");
        }

        reviewRepository.delete(review);
    }


    ///  extra end points


    // Reviews for a specific specialist (Figma: specialist profile)
    public List<Review> getReviewsBySpecialist(Integer specialistId) {

        Specialist specialist = specialistRepository.getSpecialistById(specialistId);
        if (specialist == null) {
            throw new ApiException("Specialist not found");
        }

        List<Review> reviews =
                reviewRepository.getReviewsBySpecialistId(specialistId);

        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found for this specialist");
        }

        return reviews;
    }

    // Reviews written by a specific customer (Figma: my reviews)
    public List<Review> getReviewsByCustomer(Integer customerId) {

        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        List<Review> reviews =
                reviewRepository.getReviewsByCustomerId(customerId);

        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found for this customer");
        }

        return reviews;
    }

    // Get review by id
    public Review getReviewById(Integer reviewId) {

        Review review = reviewRepository.getReviewById(reviewId);
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
