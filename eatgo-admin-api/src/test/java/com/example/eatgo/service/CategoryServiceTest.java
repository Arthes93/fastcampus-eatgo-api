package com.example.eatgo.service;

import com.example.eatgo.domain.Category;
import com.example.eatgo.repository.CategoryRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class CategoryServiceTest {

    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        categoryService = new CategoryService(categoryRepository);
    }

    @Test
    public void 카테고리_목록을_가져온다() {
        List<Category> mockCategories = new ArrayList<>();
        mockCategories.add(Category.builder()
                .name("Korean Food")
                .build());

        given(categoryRepository.findAll()).willReturn(mockCategories);
        List<Category> categories = categoryService.getCategories();

        Category category = categories.get(0);
        assertThat(category.getName()).isEqualTo("Korean Food");
    }

    @Test
    public void 카테고리를_추가한다(){
        Category category = categoryService.addCategory("Korean Food");
        verify(categoryRepository).save(any());
        assertThat(category.getName()).isEqualTo("Korean Food");
    }
}
