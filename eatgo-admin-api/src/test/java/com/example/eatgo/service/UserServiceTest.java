package com.example.eatgo.service;

import com.example.eatgo.domain.User;
import com.example.eatgo.repository.UserRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    private void setUp() {
        MockitoAnnotations.openMocks(this);

        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("유저목록을 가져온다.")
    public void getUsers() {
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(User.builder()
                .name("tester")
                .email("test@example.com")
                .level(3L)
                .build());

        given(userRepository.findAll()).willReturn(mockUsers);

        List<User> users = userService.getUsers();
        User user = users.get(0);

        assertThat(user.getName()).isEqualTo("tester");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("유저를 추가한다.")
    public void addUser(){
        String email = "test@example.com";
        String name = "tester";
        User mockUser = User.builder()
                .name(name)
                .email(email)
                .build();

        given(userRepository.save(any())).willReturn(mockUser);

        User user = userService.addUser(email, name);

        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("유저를 업데이트 한다.")
    public void updateUser(){
        Long id = 1004L;
        Long level = 3L;
        String email = "test@example.com";
        String name = "Superman";

        User mockUser = User.builder()
                .Id(id)
                .name("Administrator")
                .level(1L)
                .email(email)
                .build();

        given(userRepository.findById(id)).willReturn(Optional.of(mockUser));
        User user = userService.updateUser(id, email, name, level);
        verify(userRepository).findById(id);

        assertThat(user.getName()).isEqualTo("Superman");
        assertThat(user.isAdmin()).isEqualTo(true);
    }

    @Test @DisplayName("유저를 삭제한다.")
    public void deactiveUser(){
        Long id = 1004L;
        Long level = 2L;
        String email = "test@example.com";
        String name = "Superman";

        User mockUser = User.builder()
                .Id(id)
                .level(level)
                .email(email)
                .name(name)
                .build();
        given(userRepository.findById(id)).willReturn(Optional.of(mockUser));

        User user = userService.deactivateUser(1004L);

        verify(userRepository).findById(1004L);

        assertThat(user.isAdmin()).isEqualTo(false);
        assertThat(user.isActive()).isEqualTo(false);
    }
}