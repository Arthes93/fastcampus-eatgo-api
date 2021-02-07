package com.example.eatgo.service;

import com.example.eatgo.domain.User;
import com.example.eatgo.exception.EmailNotExistedException;
import com.example.eatgo.exception.PasswordWrongException;
import com.example.eatgo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("올바른 파라미터를 이용해 사용자 유효성 검사를 한다.")
    public void authenticateWithValidAttributes(){
        String email = "test@example.com";
        String name = "Tester";
        String password = "test";

        User mockUser = User.builder()
                .id(1004L)
                .email(email)
                .name(name)
                .password(password)
                .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(mockUser));
        given(passwordEncoder.matches(any(), any())).willReturn(true);
        User user = userService.authenticate(email, password);

        assertThat(user.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("올바르지 않은 이메일을 이용해 사용자 유효성 검사를 한다.")
    public void authenticateNotValidEmail(){
        String email = "x@example.com";
        String password = "test";

        given(userRepository.findByEmail(email)).willThrow(EmailNotExistedException.class);
        assertThatThrownBy(()->{
            User user = userService.authenticate(email, password);
        }).isInstanceOf(EmailNotExistedException.class);
    }

    @Test
    @DisplayName("올바르지 않은 이메일을 이용해 사용자 유효성 검사를 한다.")
    public void authenticateNotValidPassword(){
        String email = "test@example.com";
        String name = "Tester";
        String password = "x";
        User mockUser = User.builder()
                .id(1004L)
                .email(email)
                .name(name)
                .password(password)
                .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(mockUser));
        given(passwordEncoder.matches(password, mockUser.getPassword())).willReturn(false);
        assertThatThrownBy(()->{
            User user = userService.authenticate(email, password);
        }).isInstanceOf(PasswordWrongException.class);
    }
}