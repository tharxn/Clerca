package com.clerca.Backend.controller;

import com.clerca.Backend.dto.CalendarEventRequest;
import com.clerca.Backend.dto.CalendarEventResponse;
import com.clerca.Backend.service.CalendarEventService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/calendar")
public class CalendarEventController {

    private final CalendarEventService service;

    public CalendarEventController(CalendarEventService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CalendarEventResponse createEvent(
            @RequestBody CalendarEventRequest req,
            @AuthenticationPrincipal String email) {
        return service.createEvent(req, email);
    }

    @GetMapping("/{id}")
    public CalendarEventResponse getById(
            @PathVariable Long id,
            @AuthenticationPrincipal String email) {
        return service.getEventById(id, email);
    }

    @GetMapping("/date/{date}")
    public CalendarEventResponse getByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal String email) {
        return service.getEventByDate(date, email);
    }

    @GetMapping("/month")
    public List<CalendarEventResponse> getByMonth(
            @RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal String email) {
        return service.getEventsByMonth(year, month, email);
    }

    @PutMapping("/{id}")
    public CalendarEventResponse updateEvent(
            @PathVariable Long id,
            @RequestBody CalendarEventRequest req,
            @AuthenticationPrincipal String email) {
        return service.updateEvent(id, req, email);
    }

    @DeleteMapping("/{id}")
    public String deleteEvent(
            @PathVariable Long id,
            @AuthenticationPrincipal String email) {
        service.deleteEvent(id, email);
        return "Event deleted successfully";
    }

    // Called by Settings → Clear all data
    @DeleteMapping("/all")
    public String deleteAllEvents(@AuthenticationPrincipal String email) {
        service.deleteAllEvents(email);
        return "All events deleted";
    }
}