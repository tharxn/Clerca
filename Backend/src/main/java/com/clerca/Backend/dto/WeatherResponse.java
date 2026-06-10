package com.clerca.Backend.dto;

import lombok.*;

@Getter
@Builder
public class WeatherResponse {
    private double temperature;
    private double windspeed;
    private int weatherCode;
    private String description;
}
