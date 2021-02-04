package com.example.eatgo.controller;

import com.example.eatgo.domain.Review;
import com.example.eatgo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reivewService;

    @PostMapping("/restaurants/{restaurantId}/reviews")
    public ResponseEntity<?> create(
            @PathVariable Long restaurantId,
            @Valid @RequestBody Review resource
    ) throws URISyntaxException {
        Review review = reivewService.addReview(restaurantId, resource);

        String url = "/restaurants/" + restaurantId + "/reviews/" + review.getId();
        return ResponseEntity.created(new URI (url)).body("{}");
    }
}
