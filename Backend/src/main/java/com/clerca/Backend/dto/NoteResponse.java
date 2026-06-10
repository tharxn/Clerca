package com.clerca.Backend.dto;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Builder
public class NoteResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
