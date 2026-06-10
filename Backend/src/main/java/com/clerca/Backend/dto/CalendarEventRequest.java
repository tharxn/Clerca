package com.clerca.Backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CalendarEventRequest {
    private LocalDate eventDate;

    private String text;

    private String color;
}
