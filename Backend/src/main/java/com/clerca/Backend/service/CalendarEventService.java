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

    private final CalendarEventRepository repository;

    public CalendarEventService(CalendarEventRepository repository) {
        this.repository = repository;
    }

    public CalendarEventResponse createEvent(CalendarEventRequest req, String userEmail) {
        CalendarEvent event = CalendarEvent.builder()
                .userEmail(userEmail)
                .eventDate(req.getEventDate())
                .text(req.getText())
                .color(req.getColor())
                .build();
        return mapToResponse(repository.save(event));
    }

    public CalendarEventResponse getEventById(Long id, String userEmail) {
        CalendarEvent event = repository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        return mapToResponse(event);
    }

    public CalendarEventResponse getEventByDate(LocalDate date, String userEmail) {
        CalendarEvent event = repository.findByUserEmailAndEventDate(userEmail, date)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        return mapToResponse(event);
    }

    public List<CalendarEventResponse> getEventsByMonth(int year, int month, String userEmail) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate start = ym.atDay(1);
        LocalDate end = ym.atEndOfMonth();
        return repository.findByUserEmailAndEventDateBetween(userEmail, start, end)
                .stream().map(this::mapToResponse).toList();
    }

    public CalendarEventResponse updateEvent(Long id, CalendarEventRequest req, String userEmail) {
        CalendarEvent event = repository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        event.setEventDate(req.getEventDate());
        event.setText(req.getText());
        event.setColor(req.getColor());

        return mapToResponse(repository.save(event));
    }

    public void deleteEvent(Long id, String userEmail) {
        CalendarEvent event = repository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        repository.delete(event);
    }

    // Called by Settings → Clear all data
    public void deleteAllEvents(String userEmail) {
        repository.deleteAllByUserEmail(userEmail);
    }

    private CalendarEventResponse mapToResponse(CalendarEvent event) {
        return CalendarEventResponse.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .text(event.getText())
                .color(event.getColor())
                .build();
    }
}