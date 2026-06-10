package com.clerca.Backend.repository;

import com.clerca.Backend.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByUserEmail(String userEmail);

    Optional<Note> findByIdAndUserEmail(Long id, String userEmail);
}