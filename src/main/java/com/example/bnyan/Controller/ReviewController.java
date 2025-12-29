package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.Model.Review;
import com.example.bnyan.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /// CRUD endpoints

    @GetMapping("/get")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.status(200).body(reviewService.getAllReviews());
    }

    @PostMapping("/add/{customerId}/{specialistId}")
    public ResponseEntity<?> add(@PathVariable Integer customerId, @PathVariable Integer specialistId, @RequestBody @Valid Review review) {

        reviewService.add(customerId, specialistId, review);
        return ResponseEntity.status(200).body(new ApiResponse("Review added"));
    }
    @PutMapping("/update/{reviewId}")
    public ResponseEntity<?> update(@PathVariable Integer reviewId, @RequestBody @Valid Review review) {

        reviewService.update(reviewId, review);
        return ResponseEntity.status(200).body(new ApiResponse("Review updated"));
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<?> delete(@PathVariable Integer reviewId) {

        reviewService.delete(reviewId);
        return ResponseEntity.status(200).body(new ApiResponse("Review deleted"));
    }


    /// Extra endpoints (logic)

    @GetMapping("/get-by-specialist-id/{specialistId}")
    public ResponseEntity<?> getBySpecialist(@PathVariable Integer specialistId) {
        return ResponseEntity.status(200).body(reviewService.getReviewsBySpecialist(specialistId));
    }

    @GetMapping("/get-by-customer-id/{customerId}")
    public ResponseEntity<?> getByCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.status(200).body(reviewService.getReviewsByCustomer(customerId));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(reviewService.getReviewById(id));
    }
}
