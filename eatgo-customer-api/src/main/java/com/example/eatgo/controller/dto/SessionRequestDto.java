package com.example.eatgo.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SessionRequestDto {
    private String email;
    private String password;
}
