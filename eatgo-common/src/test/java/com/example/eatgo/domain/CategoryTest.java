package com.example.eatgo.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    public void 카테고리_객체_생성(){
        Category category = Category.builder()
                .name("Korean Food")
                .build();

        assertThat(category.getName()).isEqualTo("Korean Food");
    }
}