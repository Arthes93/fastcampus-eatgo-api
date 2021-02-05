package com.example.eatgo.controller;

import com.example.eatgo.domain.Restaurant;
import com.example.eatgo.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("/restaurants")
    public List<Restaurant> list(@RequestParam("region") String region) {
        List<Restaurant> restaurants = restaurantService.getRestaurants(region);
        return restaurants;
    }

    @GetMapping("/restaurants/{id}")
    public Restaurant detail(@PathVariable Long id) throws Exception {
        Restaurant restaurant = restaurantService.getRestaurant(id);
        return restaurant;
    }
}
