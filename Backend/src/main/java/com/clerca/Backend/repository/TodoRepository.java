package com.clerca.Backend.repository;

import com.clerca.Backend.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // All todos for a user
    List<Todo> findByUserEmail(String userEmail);

    // Active: not completed (frontend further filters out missed by dueDate)
    List<Todo> findByUserEmailAndCompletedFalse(String userEmail);

    // Completed
    List<Todo> findByUserEmailAndCompletedTrue(String userEmail);

    // Missed: not completed AND past due date
    List<Todo> findByUserEmailAndCompletedFalseAndDueDateBefore(String userEmail, LocalDateTime now);

    // Scoped find-by-id so users can't touch each other's todos
    Optional<Todo> findByIdAndUserEmail(Long id, String userEmail);
}