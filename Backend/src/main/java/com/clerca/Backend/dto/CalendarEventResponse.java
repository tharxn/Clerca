package com.clerca.Backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Builder
public class CalendarEventResponse {
    private Long id;

    private LocalDate eventDate;

    private String text;

    private String color;
}
