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

    @GetMapping("/get")
    public ResponseEntity<?> get() {
        return ResponseEntity.status(200).body(reviewService.get());
    }

    @PostMapping("/add/{customerId}/{spec_id}")
    public ResponseEntity<?> add(@PathVariable Integer customerId,@PathVariable Integer spec_id, @RequestBody @Valid Review review) {

        reviewService.add(customerId, review,spec_id);
        return ResponseEntity.status(200).body(new ApiResponse("Review added"));
    }

    @DeleteMapping("/delete/{reviewId}/{customerId}")
    public ResponseEntity<?> delete(@PathVariable Integer reviewId, @PathVariable Integer customerId) {

        reviewService.delete(reviewId, customerId);
        return ResponseEntity.status(200).body(new ApiResponse("Review deleted"));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(reviewService.getReviewById(id));
    }

    // for any one
    @GetMapping("/get-by-specialist/{spec_id}")
    public ResponseEntity<?> getBySpecialist(@PathVariable Integer spec_id) {
        return ResponseEntity.status(200).body(reviewService.getSpecialistReviews(spec_id));
    }

    // only customer
    @GetMapping("/get-by-customer/{customer_id}")
    public ResponseEntity<?> getByCustomer(@PathVariable Integer customer_id) {
        return ResponseEntity.status(200).body(reviewService.getReviewsByCustomer(customer_id));
    }

}
