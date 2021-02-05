package com.example.eatgo.service;

import com.example.eatgo.domain.Restaurant;
import com.example.eatgo.exception.RestaurantNotFoundException;
import com.example.eatgo.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

class RestaurantServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private RestaurantService restaurantService;

    @Mock
    private RestaurantRepository restaurantRepository;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockRestaurantRepository();
    }

    private void mockRestaurantRepository() {
        List<Restaurant> restaurants = new ArrayList<>();
        Restaurant restaurant = Restaurant.builder()
                .id(1004L)
                .address("Seoul")
                .name("Bob zip")
                .build();
        restaurants.add(restaurant);

        given(restaurantRepository.findAllByAddressContaining("Seoul")).willReturn(restaurants);

        given(restaurantRepository.findById(1004L))
                .willReturn(Optional.of(restaurant));
    }

    @Test
    public void 모든_레스토랑을_가져온다() {
        List<Restaurant> restaurants = restaurantService.getRestaurants("Seoul");
        Restaurant restaurant = restaurants.get(0);
        assertThat(restaurant.getId()).isEqualTo(1004L);
    }

    @Test
    public void 특정_레스토랑을_가져온다() {
        Restaurant restaurant = restaurantService.getRestaurant(1004L);

        // 레스토랑 정보를 가져오는지 확인한다.
        verify(restaurantRepository).findById(eq(1004L));

        // 가져온 정보들을 확인한다.
        // 레스토랑 정보를 확인한다.
        assertThat(restaurant.getId()).isEqualTo(1004L);
    }

    @Test
    public void 없는_레스토랑을_가져온다() {
        assertThatThrownBy(() -> {
            Restaurant restaurant = restaurantService.getRestaurant(404L);
        }).isInstanceOf(RestaurantNotFoundException.class);
//        Restaurant restaurant = restaurantService.getRestaurant(404L);
    }


    @Test
    public void 레스토랑_추가하기() {
        given(restaurantRepository.save(any())).will(invocation -> {
            Restaurant restaurant = invocation.getArgument(0);
            restaurant.setId(1234L);
            return restaurant;
        });

        Restaurant restaurant = Restaurant.builder()
                .name("BeRyong")
                .address("Busan")
                .build();

        Restaurant created = restaurantService.addRestaurant(restaurant);

        assertThat(created.getId()).isEqualTo(1234L);
    }


    @Test
    public void 레스토랑정보_업데이트() throws Exception {
        Restaurant restaurant = Restaurant.builder()
                .id(1004L)
                .name("Bob zip")
                .address("Seoul")
                .build();

        given(restaurantRepository.findById(1004L))
                .willReturn(Optional.of(restaurant));

        Restaurant updated = restaurantService.updateRestaurant(1004L, "Sool zip", "Busan");

        assertThat(updated.getName()).isEqualTo("Sool zip");
        assertThat(updated.getAddress()).isEqualTo("Busan");
    }
}