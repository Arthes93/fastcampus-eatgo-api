package com.example.eatgo.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    public void createToken(){
        String secret = "12345678901234567890123456789012";
        JwtUtil jwtUtil = new JwtUtil(secret);
        String token = jwtUtil.createToken(1004L, "");
        assertThat(token).contains(".");
    }
}