package com.example.eatgo.service;

import com.example.eatgo.domain.User;
import com.example.eatgo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User addUser(String email, String name) {
        User user = User.builder()
                .email(email)
                .name(name)
                .level(1L)
                .build();

        userRepository.save(user);
        return user;
    }

    public User updateUser(Long id, String email, String name, Long level ) {
        User user = userRepository.findById(id).orElse(null);
        user.setEmail(email);
        user.setName(name);
        user.setLevel(level);

        return user;
    }

    public User deactivateUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        user.deactivate();
        return user;
    }
}
