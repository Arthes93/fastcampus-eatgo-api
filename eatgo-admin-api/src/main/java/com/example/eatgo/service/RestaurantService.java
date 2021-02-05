package com.example.eatgo.service;

import com.example.eatgo.domain.Restaurant;
import com.example.eatgo.exception.RestaurantNotFoundException;
import com.example.eatgo.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    // 전달받은 Restaurant정보를 저장하기 위한 서비스
    public Restaurant addRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    public List<Restaurant> getRestaurants() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        return restaurants;
    }

    public Restaurant getRestaurant(Long id) {
        Optional<Restaurant> optional = restaurantRepository.findById(id);

        if (optional.isPresent()) {
            Restaurant restaurant = optional.get();
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
