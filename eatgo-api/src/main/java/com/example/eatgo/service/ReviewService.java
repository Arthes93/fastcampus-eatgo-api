package com.example.eatgo.service;

import com.example.eatgo.domain.Review;
import com.example.eatgo.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public Review addReview(Long restaurantId, Review review) {
        review.setRestaurantId(restaurantId);
        reviewRepository.save(review);
        return review;
    }
}
