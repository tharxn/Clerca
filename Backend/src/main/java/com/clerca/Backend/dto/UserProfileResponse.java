package com.clerca.Backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
public class UserProfileResponse {

    private Long id;
    private String name;
    private String email;
    private String picture;
    private LocalDateTime createdAt; 
}