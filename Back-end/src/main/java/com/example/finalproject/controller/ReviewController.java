package com.example.finalproject.controller;

import com.example.finalproject.dto.ReviewDTO;
import com.example.finalproject.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByShopId(@PathVariable Integer shopId) {
        return ResponseEntity.ok(reviewService.getReviewsByShopId(shopId));
    }

    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByAgentId(@PathVariable Integer agentId) {
        return ResponseEntity.ok(reviewService.getReviewsByAgentId(agentId));
    }

    @GetMapping("/stylist/{stylistId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByStylistId(@PathVariable Integer stylistId) {
        return ResponseEntity.ok(reviewService.getReviewsByHairstylistId(stylistId));
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.status(201).body(reviewService.createReview(reviewDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Integer id, @RequestBody ReviewDTO reviewDTO) {
        return ResponseEntity.ok(reviewService.updateReview(id, reviewDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok("✅ Review has been deleted!");
    }
}
