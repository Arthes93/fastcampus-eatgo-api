package com.example.eatgo.service;

import com.example.eatgo.domain.User;
import com.example.eatgo.exception.EmailExistedException;
import com.example.eatgo.exception.EmailNotExistedException;
import com.example.eatgo.exception.PasswordWrongException;
import com.example.eatgo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUser(String email, String name, String password) {
        // 회원이 이미 등록되어 있는지 Email을 통해 유효성 검사
        Optional<User> optional = userRepository.findByEmail(email);

        // 회원이 이미 존재하는 경우 예외처리를 한다.
        if(optional.isPresent()){
            throw new EmailExistedException(email);
        }

//        // 패스워드를 암호화해서 저장한다.
//        // 암호화 방식은 BCrypt방식을 이용해 암호화를 진행
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);

        User user = User.builder()
                .email(email)
                .name(name)
                .password(encodedPassword)
                .level(1L)
                .build();

        userRepository.save(user);

        return user;
    }

    public User authenticate(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotExistedException());

        String encodedPassword = passwordEncoder.encode(password);

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new PasswordWrongException();
        }

        return user;
    }
}
