package com.example.eatgo.controller;

import com.example.eatgo.domain.User;
import com.example.eatgo.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("유저목록을_가져온다")
    public void list() throws Exception {
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .email("tester@example.com")
                .name("tester")
                .level(1L)
                .build());

        given(userService.getUsers()).willReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("tester")));
    }

    @Test
    @DisplayName("유저를 추가한다.")
    public void create() throws Exception {
        String email = "admin@example.com";
        String name = "Administrator";
        User user = User.builder()
                .email(email)
                .name(name)
                .build();

        given(userService.addUser(email, name)).willReturn(user);

        ResultActions resultActions = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\" : \"admin@example.com\", \"name\" : \"Administrator\"}"));


        verify(userService).addUser(email, name) ;

        resultActions
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("유저상태를 업데이트 한다")
    public void update() throws Exception {
        Long id = 1004L;
        String email = "admin@example.com";
        String name = "Administrator";
        Long level = 3L;
        User user = User.builder()
                .email(email)
                .name(name)
                .level(level)
                .build();

        given(userService.updateUser(id, email, name, level)).willReturn(user);

        ResultActions resultActions = mockMvc.perform(patch("/users/1004")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"level\": 3" +
                        ",\"email\" : \"admin@example.com\"" +
                        ", \"name\" : \"Administrator\"}"));


        verify(userService).updateUser(eq(id), eq(email), eq(name), eq(level));

        resultActions
                .andExpect(status().isOk());
    }

    @Test @DisplayName("유저를 삭제한다.")
    public void deactivate() throws Exception {
        mockMvc.perform(delete("/users/1004"))
                .andExpect(status().isOk());

        verify(userService).deactivateUser(1004L);
    }
}