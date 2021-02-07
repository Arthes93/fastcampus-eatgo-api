package com.example.eatgo.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private static final String secret = "12345678901234567890123456789012";
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp(){
        jwtUtil = new JwtUtil(secret);
    }

    @Test @DisplayName("JWT를 생성한다.")
    public void createToken(){
        String token = jwtUtil.createToken(1004L, "Tester");
        assertThat(token).contains(".");
    }

    @Test
    @DisplayName("Claims를 가져온다.")
    public void getClaims(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEwMDQsIm5hbWUiOiJUZXN0ZXIifQ.I4DNdunio2m54tfUEaXHC_E-gvCQo6ZhHO15Ewkat6U";
        Claims claims = jwtUtil.getClaims(token);

        assertThat(claims.get("userId", Long.class)).isEqualTo(1004L);
        assertThat(claims.get("name")).isEqualTo("Tester");
    }
}