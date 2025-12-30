package com.example.bnyan.Controller;

import com.example.bnyan.Api.ApiResponse;
import com.example.bnyan.Model.Review;
import com.example.bnyan.Model.User;
import com.example.bnyan.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /// CRUD endpoints

    @GetMapping("/get-all")
    public ResponseEntity<List<Review>> getAll() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @PostMapping("/add/{specialistId}")
    public ResponseEntity<ApiResponse> add(@AuthenticationPrincipal User authUser, @PathVariable Integer specialistId, @RequestBody @Valid Review review) {

        reviewService.add(authUser, specialistId, review);
        return ResponseEntity.ok(new ApiResponse("Review added"));
    }

    @PutMapping("/update/{reviewId}")
    public ResponseEntity<ApiResponse> update(@AuthenticationPrincipal User authUser, @PathVariable Integer reviewId, @RequestBody @Valid Review review) {

        reviewService.update(authUser, reviewId, review);
        return ResponseEntity.ok(new ApiResponse("Review updated"));
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<ApiResponse> delete(@AuthenticationPrincipal User authUser, @PathVariable Integer reviewId) {

        reviewService.delete(authUser, reviewId);
        return ResponseEntity.ok(new ApiResponse("Review deleted"));
    }

    /// Extra endpoints

    @GetMapping("/get-by-specialist-id/{specialistId}")
    public ResponseEntity<List<Review>> getBySpecialist(@PathVariable Integer specialistId) {
        return ResponseEntity.ok(reviewService.getReviewsBySpecialist(specialistId));
    }

    @GetMapping("/get-by-id/{reviewId}")
    public ResponseEntity<Review> getById(@PathVariable Integer reviewId) {
        return ResponseEntity.ok(reviewService.getReviewById(reviewId));
    }

    @GetMapping("/my-reviews")
    public ResponseEntity<List<Review>> getMyReviews(@AuthenticationPrincipal User authUser) {
        return ResponseEntity.ok(reviewService.getReviewsByCustomer(authUser.getId()));
    }
}
