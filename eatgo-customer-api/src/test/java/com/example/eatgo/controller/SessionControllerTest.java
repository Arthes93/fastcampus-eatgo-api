package com.example.eatgo.controller;

import com.example.eatgo.domain.User;
import com.example.eatgo.exception.EmailNotExistedException;
import com.example.eatgo.exception.PasswordWrongException;
import com.example.eatgo.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("AccessToken을 반환하는지 확인한다.")
    public void create() throws Exception {
        String email = "tester@example.com";
        String password = "test";

        User mockUser = User.builder().password("ACCESSTOKEN").build();

        given(userService.authenticate(email, password)).willReturn(mockUser);

        ResultActions resultActions = mockMvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester@example.com\",\"password\":\"test\"}"));

        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/session"))
                // AccessToken을 사용하는지 확인한다.
                .andExpect(content().string("{\"accessToken\":\"ACCESSTOKE\"}"));


        verify(userService).authenticate(eq(email), eq(password));
    }

    @Test
    @DisplayName("올바르지 않은 이메일을 이용한 요청을 시도")
    public void createWithNotExistedEmail() throws Exception {
        String email = "x@example.com";
        String password = "test";

        given(userService.authenticate(email, password)).willThrow(EmailNotExistedException.class);

        ResultActions resultActions = mockMvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"x@example.com\",\"password\":\"test\"}"));

        resultActions
                .andExpect(status().isBadRequest());

        verify(userService).authenticate(eq(email), eq(password));
    }

    @Test
    @DisplayName("올바르지 않은 패스워드를 이용한 요청을 시도")
    public void createWithInvalidAttributes() throws Exception {
        String email = "tester@example.com";
        String password = "x";

        given(userService.authenticate(email, password)).willThrow(PasswordWrongException.class);

        ResultActions resultActions = mockMvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"tester@example.com\",\"password\":\"x\"}"));

        resultActions
                .andExpect(status().isBadRequest());

        verify(userService).authenticate(eq(email), eq(password));
    }
}