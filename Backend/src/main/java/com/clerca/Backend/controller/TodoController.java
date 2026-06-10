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

    // Returns only the authenticated user's active (not completed) todos
    @GetMapping("/active")
    public ResponseEntity<List<TodoResponse>> getActiveTodos(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(todoService.getActiveTodos(email));
    }

    // Returns only the authenticated user's completed todos
    @GetMapping("/completed")
    public ResponseEntity<List<TodoResponse>> getCompletedTodos(
            @AuthenticationPrincipal String email) {
        return ResponseEntity.ok(todoService.getCompletedTodos(email));
    }

    // Returns only the authenticated user's missed todos (past due, not completed)
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
}