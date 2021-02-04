package com.example.eatgo.exception;


public class RestaurantNotFoundException extends RuntimeException{
    public RestaurantNotFoundException(Long id) {
        super("Could not find Restaurant" + id );
    }
}
