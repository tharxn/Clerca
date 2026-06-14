package com.clerca.Backend.controller;

import com.clerca.Backend.dto.NoteRequest;
import com.clerca.Backend.dto.NoteResponse;
import com.clerca.Backend.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public NoteResponse createNote(
            @Valid @RequestBody NoteRequest request,
            @AuthenticationPrincipal String email) {
        return noteService.createNote(request, email);
    }

    @GetMapping
    public List<NoteResponse> getAllNotes(
            @AuthenticationPrincipal String email) {
        return noteService.getAllNotes(email);
    }

    @GetMapping("/{id}")
    public NoteResponse getNoteById(
            @PathVariable Long id,
            @AuthenticationPrincipal String email) {
        return noteService.getNoteById(id, email);
    }

    @PutMapping("/{id}")
    public NoteResponse updateNote(
            @PathVariable Long id,
            @Valid @RequestBody NoteRequest request,
            @AuthenticationPrincipal String email) {
        return noteService.updatedNote(id, request, email);
    }

    @DeleteMapping("/{id}")
    public String deleteNote(
            @PathVariable Long id,
            @AuthenticationPrincipal String email) {
        noteService.deleteNote(id, email);
        return "Note deleted successfully";
    }

    // Called by Settings → Clear all data
    @DeleteMapping("/all")
    public String deleteAllNotes(@AuthenticationPrincipal String email) {
        noteService.deleteAllNotes(email);
        return "All notes deleted";
    }
}