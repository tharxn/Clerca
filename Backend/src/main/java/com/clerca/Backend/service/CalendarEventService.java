package com.clerca.Backend.service;

import com.clerca.Backend.dto.CalendarEventRequest;
import com.clerca.Backend.dto.CalendarEventResponse;
import com.clerca.Backend.entity.CalendarEvent;
import com.clerca.Backend.exception.ResourceNotFoundException;
import com.clerca.Backend.repository.CalendarEventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
public class CalendarEventService {

    private final CalendarEventRepository repo;

    public CalendarEventService(CalendarEventRepository repo) {
        this.repo = repo;
    }

    public CalendarEventResponse createEvent(CalendarEventRequest req, String userEmail) {
        // One event per date per user — reject duplicates
        repo.findByUserEmailAndEventDate(userEmail, req.getEventDate()).ifPresent(existing -> {
            throw new IllegalArgumentException(
                    "An event already exists for " + req.getEventDate() + ". Use PUT to update it.");
        });

        CalendarEvent saved = repo.save(CalendarEvent.builder()
                .userEmail(userEmail)
                .eventDate(req.getEventDate())
                .text(req.getText())
                .color(req.getColor())
                .build());
        return toResponse(saved);
    }

    public CalendarEventResponse getEventById(Long id, String userEmail) {
        CalendarEvent event = repo.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        return toResponse(event);
    }

    public CalendarEventResponse getEventByDate(LocalDate date, String userEmail) {
        CalendarEvent event = repo.findByUserEmailAndEventDate(userEmail, date)
                .orElseThrow(() -> new ResourceNotFoundException("No event on " + date));
        return toResponse(event);
    }

    public List<CalendarEventResponse> getEventsByMonth(int year, int month, String userEmail) {
        YearMonth ym = YearMonth.of(year, month);
        return repo.findByUserEmailAndEventDateBetween(userEmail, ym.atDay(1), ym.atEndOfMonth())
                .stream().map(this::toResponse).toList();
    }

    public CalendarEventResponse updateEvent(Long id, CalendarEventRequest req, String userEmail) {
        CalendarEvent event = repo.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        event.setText(req.getText());
        event.setColor(req.getColor());
        return toResponse(repo.save(event));
    }

    public void deleteEvent(Long id, String userEmail) {
        CalendarEvent event = repo.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id: " + id));
        repo.delete(event);
    }

    private CalendarEventResponse toResponse(CalendarEvent e) {
        return CalendarEventResponse.builder()
                .id(e.getId())
                .eventDate(e.getEventDate())
                .text(e.getText())
                .color(e.getColor())
                .build();
    }
}