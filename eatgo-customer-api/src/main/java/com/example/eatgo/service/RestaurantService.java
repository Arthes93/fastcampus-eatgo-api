package com.example.eatgo.service;

import com.example.eatgo.domain.MenuItem;
import com.example.eatgo.domain.Restaurant;
import com.example.eatgo.domain.Review;
import com.example.eatgo.exception.RestaurantNotFoundException;
import com.example.eatgo.repository.MenuItemRepository;
import com.example.eatgo.repository.RestaurantRepository;
import com.example.eatgo.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final ReviewRepository reviewRepository;

    // 전달받은 Restaurant정보를 저장하기 위한 서비스
    public Restaurant addRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    public List<Restaurant> getRestaurants(String region) {
        List<Restaurant> restaurants = restaurantRepository.findAllByAddressContaining(region);
        return restaurants;
    }

    public Restaurant getRestaurant(Long id) {
        Optional<Restaurant> optional = restaurantRepository.findById(id);

        if (optional.isPresent()) {
            Restaurant restaurant = optional.get();

            // 해당 restaurant id값을 갖는 모든 Menu들을 가져온다.
            List<MenuItem> menuItems = menuItemRepository.findAllByRestaurantId(id);
            restaurant.setMenuItems(menuItems);

            // 해당 restaurant id값을 갖는 모든 review들을 가져온다.
            List<Review> reviews = reviewRepository.findAllByRestaurantId(id);
            restaurant.setReviews(reviews);
            return restaurant;
        }else{
            throw new RestaurantNotFoundException(id);
        }
    }

    @Transactional
    public Restaurant updateRestaurant(long id, String name, String address) throws Exception {
        Optional<Restaurant> optional = restaurantRepository.findById(id);

        if (optional.isPresent()) {
            Restaurant restaurant = optional.get();
            restaurant.setName(name);
            restaurant.setAddress(address);

            return restaurant;
        } else {
            throw new RestaurantNotFoundException(id);
        }
    }
}
