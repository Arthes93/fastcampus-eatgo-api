package com.example.eatgo.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTests {

    @Test
    public void User객체를_생성(){
        User user = User.builder()
                .email("tester@example.com")
                .name("테스터")
                .level(1L)
                .build();

        assertThat(user.getName()).isEqualTo("테스터");
        assertThat(user.isAdmin()).isEqualTo(true);
    }

    @Test
    public void accessToken(){
        User user = User.builder()
                .password("ACCESSTOKEN")
                .build();
    }
}
