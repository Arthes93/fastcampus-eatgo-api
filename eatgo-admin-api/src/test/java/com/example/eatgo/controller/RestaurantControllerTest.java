package com.example.eatgo.controller;

import com.example.eatgo.domain.Restaurant;
import com.example.eatgo.exception.RestaurantNotFoundException;
import com.example.eatgo.service.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
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
    public void list를_확인한다() throws Exception {
        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(Restaurant.builder()
                .id(1004L)
                .name("JOKER House")
                .address("Seoul")
                .build());

        given(restaurantService.getRestaurants()).willReturn(restaurants);

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
                .name("JOKER House")
                .address("Seoul")
                .build();
        given(restaurantService.getRestaurant(1004L)).willReturn(restaurant);

        ResultActions resultActions = mockMvc.perform(get("/restaurants/1004"));

        resultActions
                .andExpect(status().isOk())
                // 레스토랑 정보가 들어있는지 확인한다.
                .andExpect(content().string(containsString("\"id\":1004")))
                .andExpect(content().string(containsString("\"name\":\"JOKER House\"")));
    }

    @Test
    public void 없는_페이지에대한_예외처리를_한다() throws Exception {
        given(restaurantService.getRestaurant(404L)).willThrow(new RestaurantNotFoundException(404L));
        ResultActions resultActions = mockMvc.perform(get("/restaurants/404"));

        resultActions
                .andExpect(status().isNotFound())
                .andExpect(content().string("{}"));
    }

    @Test
    public void 새로운_레스토랑_저장하기() throws Exception {
        String BobZip = "BobZip";
        String Seoul = "Seoul";

        Restaurant restaurant = Restaurant.builder()
                .name(BobZip)
                .address(Seoul)
                .build();

        String content = objectMapper.writeValueAsString(restaurant);

        ResultActions resultActions = mockMvc.perform(post("/restaurants")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions
                .andExpect(status().isCreated())
                .andDo(print());

    }

    @Test
    public void 새로운_레스토랑_저장_및_조회하기() throws Exception {
        String BobZip = "BobZip";
        String Seoul = "Seoul";

        Restaurant restaurant = Restaurant.builder()
                .name(BobZip)
                .address(Seoul)
                .build();

        String content = objectMapper.writeValueAsString(restaurant);

        ResultActions postResultActions = mockMvc.perform(post("/restaurants")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));

        postResultActions
                .andExpect(status().isCreated())
                .andDo(print());


        List<Restaurant> restaurants = new ArrayList<>();
        restaurants.add(restaurant);
        given(restaurantService.getRestaurants()).willReturn(restaurants);

        ResultActions getResultActions = mockMvc.perform(get("/restaurants"));

        getResultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(BobZip))
                .andExpect(jsonPath("$[0].address").value(Seoul))
                .andDo(print());
    }

    @Test
    public void 레스토랑정보_업데이트() throws Exception {
        String JOKER = "JOKER Bar";
        String Busan = "Busan";

        Restaurant restaurant = Restaurant.builder()
                .name(JOKER)
                .address(Busan)
                .build();

        String content = objectMapper.writeValueAsString(restaurant);

        ResultActions resultActions = mockMvc.perform(patch("/restaurants/1004")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        resultActions
                .andExpect(status().isOk())
                .andDo(print());

        verify(restaurantService).updateRestaurant(1004L, JOKER, Busan);
    }
}