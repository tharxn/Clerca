package com.clerca.Backend.service;

import com.clerca.Backend.dto.NoteRequest;
import com.clerca.Backend.dto.NoteResponse;
import com.clerca.Backend.entity.Note;
import com.clerca.Backend.exception.ResourceNotFoundException;
import com.clerca.Backend.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public NoteResponse createNote(NoteRequest request, String userEmail) {
        String title = request.getTitle();
        if (title == null || title.trim().isEmpty()) {
            title = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
        Note note = Note.builder()
                .title(title)
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .userEmail(userEmail)
                .build();
        return mapToResponse(noteRepository.save(note));
    }

    public List<NoteResponse> getAllNotes(String userEmail) {
        return noteRepository.findByUserEmail(userEmail)
                .stream().map(this::mapToResponse).toList();
    }

    public NoteResponse getNoteById(Long id, String userEmail) {
        Note note = noteRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        return mapToResponse(note);
    }

    public NoteResponse updatedNote(Long id, NoteRequest request, String userEmail) {
        Note note = noteRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        return mapToResponse(noteRepository.save(note));
    }

    public void deleteNote(Long id, String userEmail) {
        Note note = noteRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Note not found"));
        noteRepository.delete(note);
    }

    // Called by Settings → Clear all data
    public void deleteAllNotes(String userEmail) {
        noteRepository.deleteAllByUserEmail(userEmail);
    }

    private NoteResponse mapToResponse(Note note) {
        return NoteResponse.builder()
                .id(note.getId())
                .title(note.getTitle())
                .content(note.getContent())
                .createdAt(note.getCreatedAt())
                .build();
    }
}