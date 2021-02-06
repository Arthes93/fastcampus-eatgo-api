package com.example.eatgo.controller;

import com.example.eatgo.domain.User;
import com.example.eatgo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    // 1. User list
    // 2. User create -> 회원가입
    // 3. User update
    // 4. User delete -> level: 0 => 아무것도 못 함.
    // (1: customer 2: restaurant owner 3: admin)

    private final UserService userService;

    @GetMapping("/users")
    public List<User> list(){
        return userService.getUsers();
    }

    @PostMapping("/users")
    public ResponseEntity<?> create(@RequestBody User resource) throws URISyntaxException {
        User user = userService.addUser(resource.getEmail(), resource.getName());
        String url = "/users" + user.getId();
        return ResponseEntity.created(URI.create(url)).body("{}");
    }

    @PatchMapping("/users/{userId}")
    public void udpate(@PathVariable("userId") Long id, @RequestBody User user){
        userService.updateUser(id, user.getEmail(), user.getName(), user.getLevel());
    }

    @DeleteMapping("/users/{userId}")
    public String deactivate(@PathVariable("userId")Long userId){
        userService.deactivateUser(userId);
        return "{}";
    }
}
