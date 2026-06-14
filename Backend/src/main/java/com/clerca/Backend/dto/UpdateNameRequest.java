package com.clerca.Backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateNameRequest {

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name must be 100 characters or fewer")
    private String name;
}