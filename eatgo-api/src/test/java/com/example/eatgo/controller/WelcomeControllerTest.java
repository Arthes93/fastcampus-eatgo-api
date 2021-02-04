package com.example.eatgo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
class WelcomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void Hello_world를_확인한다() throws Exception{
        ResultActions resultActions = mockMvc.perform(get("/"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, world"))
                .andDo(print());
    }
}