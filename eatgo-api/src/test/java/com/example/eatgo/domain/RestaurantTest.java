package com.example.eatgo.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantTest {

    @Test
    public void creation() {
        Long id = 1004L;
        String name = "OutBack";
        String address = "Seoul";
        Restaurant restaurant = Restaurant.builder()
                .id(id)
                .name(name)
                .address(address)
                .build();

        assertThat(restaurant).isNotNull();
        assertThat(restaurant.getName()).isEqualTo(name);
        assertThat(restaurant.getAddress()).isEqualTo(address);
    }
}