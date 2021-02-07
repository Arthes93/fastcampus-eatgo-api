package com.example.eatgo.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionResponseDto {

    private String accessToken;
}
