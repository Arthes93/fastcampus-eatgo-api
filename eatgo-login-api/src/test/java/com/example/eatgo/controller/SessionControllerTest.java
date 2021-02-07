package com.example.eatgo.controller;

import com.example.eatgo.domain.User;
import com.example.eatgo.exception.EmailNotExistedException;
import com.example.eatgo.exception.PasswordWrongException;
import com.example.eatgo.service.UserService;
import com.example.eatgo.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("AccessToken을 반환하는지 확인한다.")
    public void create() throws Exception {
        Long id = 1004L;
        String email = "tester@example.com";
        String password = "test";
        String name = "Tester";

        User mockUser = User.builder()
                .id(id)
                .email(email)
                .name(name)
                .password(password)
                .level(3L)
                .build();

        String content = objectMapper.writeValueAsString(mockUser);

        given(userService.authenticate(email, password)).willReturn(mockUser);
        given(jwtUtil.createToken(id, name, null)).willReturn("header.payload.signature");

        ResultActions resultActions = mockMvc.perform(post("/session")
                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"email\":\"tester@example.com\",\"password\":\"test\"}")
                .content(content)
        );

        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/session"))
                // AccessToken을 사용하는지 확인한다.
                .andExpect(content().string(containsString("{\"accessToken\":\"header.payload.signature\"}")));


        verify(userService).authenticate(eq(email), eq(password));
    }

    @Test
    @DisplayName("AccessToken에 ResturantId를 넣 반환하는지 확인한다.")
    public void createRestaurantOwner() throws Exception {
        Long id = 1004L;
        String email = "tester@example.com";
        String password = "test";
        String name = "Tester";
        Long level = 50L;
        Long restaurantId = 369L;

        User mockUser = User.builder()
                .id(id)
                .email(email)
                .name(name)
                .password(password)
                .level(level)
                .restaurantId(restaurantId)
                .build();

        String content = objectMapper.writeValueAsString(mockUser);

        given(userService.authenticate(email, password)).willReturn(mockUser);
        given(jwtUtil.createToken(id, name, 369L)).willReturn("header.payload.signature");

        ResultActions resultActions = mockMvc.perform(post("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
        );

        resultActions
                .andExpect(status().isCreated())
                .andExpect(header().string("location", "/session"))
                // AccessToken을 사용하는지 확인한다.
                .andExpect(content().string(containsString("{\"accessToken\":\"header.payload.signature\"}")));


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