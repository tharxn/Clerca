package com.clerca.Backend.repository;

import com.clerca.Backend.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUserEmail(String userEmail);

    Optional<Note> findByIdAndUserEmail(Long id, String userEmail);

    // Used by Settings → Clear all data
    @Modifying
    @Transactional
    @Query("DELETE FROM Note n WHERE n.userEmail = :userEmail")
    void deleteAllByUserEmail(@Param("userEmail") String userEmail);
}