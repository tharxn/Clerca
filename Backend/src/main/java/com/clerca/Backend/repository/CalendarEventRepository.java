package com.clerca.Backend.repository;

import com.clerca.Backend.entity.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

    // Scoped to one user — one event per date per user
    Optional<CalendarEvent> findByUserEmailAndEventDate(String userEmail, LocalDate eventDate);

    // Month range scoped to one user
    List<CalendarEvent> findByUserEmailAndEventDateBetween(String userEmail, LocalDate start, LocalDate end);

    // Scoped find-by-id
    Optional<CalendarEvent> findByIdAndUserEmail(Long id, String userEmail);
}