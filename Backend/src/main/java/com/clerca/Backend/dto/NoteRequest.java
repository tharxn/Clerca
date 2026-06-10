package com.clerca.Backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
public class NoteRequest {

    private String title;
    private String content;
}
