package com.clerca.Backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "calendar_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The email/subject from the JWT — scopes this event to one user
    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private LocalDate eventDate;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false)
    private String color;
}