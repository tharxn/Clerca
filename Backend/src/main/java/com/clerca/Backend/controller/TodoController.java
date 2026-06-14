package com.clerca.Backend.controller;

import com.clerca.Backend.dto.TodoRequest;
import com.clerca.Backend.dto.TodoResponse;
import com.clerca.Backend.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/active")
    public ResponseEntity<List<TodoResponse>> getActiveTodos(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(todoService.getActiveTodos(email));
    }

    @GetMapping("/completed")
    public ResponseEntity<List<TodoResponse>> getCompletedTodos(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(todoService.getCompletedTodos(email));
    }

    @GetMapping("/missed")
    public ResponseEntity<List<TodoResponse>> getMissedTodos(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(todoService.getMissedTodos(email));
    }

    @PostMapping
    public ResponseEntity<TodoResponse> createTodo(
            @RequestBody TodoRequest request,
            @AuthenticationPrincipal String email) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(todoService.createTodo(request, email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponse> updateTodo(
            @PathVariable Long id,
            @RequestBody TodoRequest request,
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(todoService.updateTodo(id, request, email));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<TodoResponse> toggleCompleted(
            @PathVariable Long id,
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(todoService.toggleComplete(id, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable Long id,
            @AuthenticationPrincipal String email) {
        todoService.deleteTodo(id, email);
        return ResponseEntity.noContent().build();
    }

    // Called by Settings → Clear all data
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllTodos(
            @AuthenticationPrincipal String email) {
        todoService.deleteAllTodos(email);
        return ResponseEntity.noContent().build();
    }
}