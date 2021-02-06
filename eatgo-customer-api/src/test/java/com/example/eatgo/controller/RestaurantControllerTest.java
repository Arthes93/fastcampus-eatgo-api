package com.example.eatgo.controller;

import com.example.eatgo.domain.MenuItem;
import com.example.eatgo.domain.Restaurant;
import com.example.eatgo.domain.Review;
import com.example.eatgo.exception.RestaurantNotFoundException;
import com.example.eatgo.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
@AutoConfigureMockMvc
class RestaurantControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantService restaurantService;

    @Test
    public void 레스토랑_전체를_확인한다() throws Exception {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(Restaurant.builder()
                .id(1004L)
                .categoryId(1L)
                .name("JOKER House")
                .address("Seoul")
                .build());

        Long categoryId = 1L;
        given(restaurantService.getRestaurants("Seoul", categoryId)).willReturn(restaurants);

        ResultActions resultActions = mockMvc.perform(get("/restaurants?region=Seoul&category=1"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1004")))
                .andExpect(content().string(containsString("\"name\":\"JOKER House\"")))
                .andDo(print());
    }

    @Test
    public void RequestParameter를_이용한_list를_확인한다() throws Exception {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(Restaurant.builder()
                .id(1004L)
                .categoryId(1L)
                .name("JOKER House")
                .address("Seoul")
                .build());

        given(restaurantService.getRestaurants("Seoul", 1L)).willReturn(restaurants);

        ResultActions resultActions = mockMvc.perform(get("/restaurants?region=Seoul&category=1"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1004")))
                .andExpect(content().string(containsString("\"name\":\"JOKER House\"")))
                .andDo(print());
    }

    @Test
    public void 특정_가게_상세정보를_가져온다() throws Exception {
        Restaurant restaurant = Restaurant.builder()
                .id(1004L)
                .categoryId(1L)
                .name("JOKER House")
                .address("Seoul")
                .build();
        // 메뉴 정보를 생성
        MenuItem menuItem = MenuItem.builder()
                .name("Kimchi")
                .build();
        // 리뷰 정보를 생성
        Review review = Review.builder()
                .name("JOKER")
                .score(5)
                .description("Great!")
                .build();
        // 레스토랑 객체에 메뉴 정보들을 저장한다.
        restaurant.setMenuItems(Arrays.asList(menuItem));
        // 레스토랑 객체에 리뷰 정보들을 저장한다.
        restaurant.setReviews(Arrays.asList(review));


        given(restaurantService.getRestaurant(1004L)).willReturn(restaurant);

        ResultActions resultActions = mockMvc.perform(get("/restaurants/1004"));

        resultActions
                .andExpect(status().isOk())
                // 레스토랑 정보가 들어있는지 확인한다.
                .andExpect(content().string(containsString("\"id\":1004")))
                .andExpect(content().string(containsString("\"name\":\"JOKER House\"")))
                // 메뉴 정보가 들어있는지 확인한다.
                .andExpect(content().string(containsString("Kimchi")))
                // 리뷰 정보가 들어있는지 확인한다.
                .andExpect(content().string(containsString("Great!")))
                .andDo(print());

    }

    @Test
    public void 없는_페이지에대한_예외처리를_한다() throws Exception{
        given(restaurantService.getRestaurant(404L)).willThrow(new RestaurantNotFoundException(404L));
        ResultActions resultActions = mockMvc.perform(get("/restaurants/404"));

        resultActions
                .andExpect(status().isNotFound())
                .andExpect(content().string("{}"));
    }
}