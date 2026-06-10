package com.clerca.Backend.dto;

import lombok.*;

@Getter
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String name;
}
