package com.example.eatgo.controller;

import com.example.eatgo.controller.dto.SessionRequestDto;
import com.example.eatgo.controller.dto.SessionResponseDto;
import com.example.eatgo.domain.User;
import com.example.eatgo.service.UserService;
import com.example.eatgo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class SessionController {

    private final JwtUtil jwtUtil;

    private final UserService userService;


    @PostMapping("/session")
    public ResponseEntity<SessionResponseDto> create(@RequestBody SessionRequestDto resource){
        String email = resource.getEmail();
        String password = resource.getPassword();
        User user = userService.authenticate(email, password);

        String url = "/session";
        String accessToken = jwtUtil.createToken(user.getId(), user.getName(),
                user.isRestaurantOwner() ? user.getRestaurantId() : null);

        SessionResponseDto sessionResponseDto = SessionResponseDto.builder()
                .accessToken(accessToken)
                .build();

        return ResponseEntity.created(URI.create(url)).body(sessionResponseDto);
    }
}
