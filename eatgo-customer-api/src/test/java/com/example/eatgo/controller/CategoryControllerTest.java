package com.example.eatgo.controller;

import com.example.eatgo.domain.Category;
import com.example.eatgo.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    public void 카테고리들_가져오기() throws Exception {
        List<Category> categories = new ArrayList<>();
        categories.add(Category.builder()
                .name("Korean Food")
                .build());

        given(categoryService.getCategories()).willReturn(categories);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Korean Food")));
    }


    @Test
    public void 카테고리_생성하기() throws Exception {
        Category category = Category.builder()
                .name("Korean Food")
                .build();

        given(categoryService.addCategory("Korean Food")).willReturn(category);

        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\" :\"Korean Food\" }"))
                .andExpect(status().isCreated())
                .andExpect(content().string("{}"));
    }
}