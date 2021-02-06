package com.example.eatgo.service;

import com.example.eatgo.domain.User;
import com.example.eatgo.exception.EmailExistedException;
import com.example.eatgo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test @DisplayName("사용자를 등록한다.")
    public void registerUser(){
        String email = "test@example.com";
        String name = "Tester";
        String password = "test";

        User user = userService.registerUser(email, name, password);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getPassword()).isEqualTo(password);

        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("사용자가 이미 등록돼 있는경우 예외처리를 한다.")
    public void registerUserWithExistedEmail(){
        String email = "test@example.com";
        String name = "Tester";
        String password = "test";
        User mockUser = User.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(mockUser));

        // 예외처리가 발생되는지 확인한다.
        assertThatThrownBy(() -> {
            userService.registerUser(email, name, password);
        }).isInstanceOf(EmailExistedException.class);

        verify(userRepository).findByEmail(any());
    }
}