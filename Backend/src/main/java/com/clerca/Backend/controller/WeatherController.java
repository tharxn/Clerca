package com.clerca.Backend.controller;

import com.clerca.Backend.dto.WeatherResponse;
import com.clerca.Backend.service.WeatherService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping
    public WeatherResponse getWeather(
            @RequestParam double lat,
            @RequestParam double lon) {
        return weatherService.getWeather(lat, lon);
    }
}