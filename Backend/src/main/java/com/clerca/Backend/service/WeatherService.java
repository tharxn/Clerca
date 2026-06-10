package com.clerca.Backend.service;

import com.clerca.Backend.dto.WeatherResponse;
import com.clerca.Backend.util.WeatherCodeUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherResponse getWeather(double lat, double lon) {
        String url = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&current_weather=true",
                lat, lon);

        // Step 1: get raw JSON string
        String raw = restTemplate.getForObject(url, String.class);

        // Step 2: parse the string into a tree
        JsonNode response;
        try {
            response = objectMapper.readTree(raw);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse weather response", e);
        }

        JsonNode current = response.get("current_weather");
        int code = current.get("weathercode").asInt();

        return WeatherResponse.builder()
                .temperature(current.get("temperature").asDouble())
                .windspeed(current.get("windspeed").asDouble())
                .weatherCode(code)
                .description(WeatherCodeUtil.describe(code))
                .build();
    }
}