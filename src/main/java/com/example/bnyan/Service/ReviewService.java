package com.example.bnyan.Service;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.Model.Customer;
import com.example.bnyan.Model.Review;
import com.example.bnyan.Model.Specialist;
import com.example.bnyan.Model.User;
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

    /// CRUD operations

    public List<Review> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found");
        }
        return reviews;
    }

    public void add(User authUser, Integer specialistId, Review review) {
        if (!authUser.getRole().equals("USER")) {
            throw new ApiException("Only customers can create reviews");
        }

        Customer customer = customerRepository.getCustomerById(authUser.getId());
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        Specialist specialist = specialistRepository.findSpecialistById(specialistId);
        if (specialist == null) {
            throw new ApiException("Specialist not found");
        }

        // Verify the customer worked with the specialist
        boolean workedWith = projectRepository.findProjectsByCustomer(customer)
                .stream()
                .flatMap(project -> project.getSpecialists().stream())
                .anyMatch(specialist::equals);

        if (!workedWith) {
            throw new ApiException("You cannot review a specialist you did not work with");
        }

        // Check if a review already exists
        Review existing = reviewRepository.getReviewByCustomerIdAndSpecialistId(customer.getId(), specialistId);
        if (existing != null) {
            throw new ApiException("You already reviewed this specialist");
        }

        review.setCustomer(customer);
        review.setSpecialist(specialist);

        reviewRepository.save(review);
    }

    public void update(User authUser, Integer reviewId, Review newReview) {
        Review review = reviewRepository.getReviewById(reviewId);
        if (review == null) {
            throw new ApiException("Review not found");
        }

        //only the review owner can update
        if (!authUser.getRole().equals("ADMIN") && !review.getCustomer().getId().equals(authUser.getId())) {
            throw new ApiException("Unauthorized to update this review");
        }

        review.setRate(newReview.getRate());
        review.setComment(newReview.getComment());

        reviewRepository.save(review);
    }

    public void delete(User authUser, Integer reviewId) {
        Review review = reviewRepository.getReviewById(reviewId);
        if (review == null) {
            throw new ApiException("Review not found");
        }

        //only the review owner can delete
        if (!authUser.getRole().equals("ADMIN") && !review.getCustomer().getId().equals(authUser.getId())) {
            throw new ApiException("Unauthorized to delete this review");
        }

        reviewRepository.delete(review);
    }

    /// Extra endpoints

    public List<Review> getReviewsBySpecialist(Integer specialistId) {
        Specialist specialist = specialistRepository.getSpecialistById(specialistId);
        if (specialist == null) {
            throw new ApiException("Specialist not found");
        }

        List<Review> reviews = reviewRepository.getReviewsBySpecialistId(specialistId);
        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found for this specialist");
        }

        return reviews;
    }

    public Review getReviewById(Integer reviewId) {
        Review review = reviewRepository.getReviewById(reviewId);
        if (review == null) {
            throw new ApiException("Review not found");
        }
        return review;
    }

    public List<Review> getSpecialistReviews(Integer specialistId) {
        Specialist specialist = specialistRepository.findSpecialistById(specialistId);
        if (specialist == null) {
            throw new ApiException("Specialist not found");
        }
        return reviewRepository.findReviewsBySpecialist(specialist);
    }

    public List<Review> getReviewsByCustomer(Integer customerId) {
        Customer customer = customerRepository.getCustomerById(customerId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        return reviewRepository.findReviewsByCustomer(customer);
    }
}
