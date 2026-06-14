package com.clerca.Backend.repository;

import com.clerca.Backend.entity.CalendarEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Long> {

    Optional<CalendarEvent> findByUserEmailAndEventDate(String userEmail, LocalDate eventDate);

    List<CalendarEvent> findByUserEmailAndEventDateBetween(String userEmail, LocalDate start, LocalDate end);

    Optional<CalendarEvent> findByIdAndUserEmail(Long id, String userEmail);

    // Used by Settings → Clear all data
    @Modifying
    @Transactional
    @Query("DELETE FROM CalendarEvent c WHERE c.userEmail = :userEmail")
    void deleteAllByUserEmail(@Param("userEmail") String userEmail);
}