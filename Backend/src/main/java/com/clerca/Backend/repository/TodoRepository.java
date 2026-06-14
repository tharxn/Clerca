package com.clerca.Backend.repository;

import com.clerca.Backend.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByUserEmail(String userEmail);

    List<Todo> findByUserEmailAndCompletedFalse(String userEmail);

    List<Todo> findByUserEmailAndCompletedTrue(String userEmail);

    List<Todo> findByUserEmailAndCompletedFalseAndDueDateBefore(String userEmail, LocalDateTime now);

    Optional<Todo> findByIdAndUserEmail(Long id, String userEmail);

    // Used by Settings → Clear all data
    @Modifying
    @Transactional
    @Query("DELETE FROM Todo t WHERE t.userEmail = :userEmail")
    void deleteAllByUserEmail(@Param("userEmail") String userEmail);
}