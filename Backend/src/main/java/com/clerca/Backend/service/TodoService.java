package com.clerca.Backend.service;

import com.clerca.Backend.dto.TodoRequest;
import com.clerca.Backend.dto.TodoResponse;
import com.clerca.Backend.entity.Todo;
import com.clerca.Backend.exception.ResourceNotFoundException;
import com.clerca.Backend.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public List<TodoResponse> getActiveTodos(String userEmail) {
        return todoRepository.findByUserEmailAndCompletedFalse(userEmail)
                .stream().map(this::mapToResponse).toList();
    }

    public List<TodoResponse> getCompletedTodos(String userEmail) {
        return todoRepository.findByUserEmailAndCompletedTrue(userEmail)
                .stream().map(this::mapToResponse).toList();
    }

    public List<TodoResponse> getMissedTodos(String userEmail) {
        return todoRepository.findByUserEmailAndCompletedFalseAndDueDateBefore(userEmail, LocalDateTime.now())
                .stream().map(this::mapToResponse).toList();
    }

    public TodoResponse createTodo(TodoRequest request, String userEmail) {
        Todo todo = Todo.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority() != null ? request.getPriority() : Todo.Priority.MEDIUM)
                .dueDate(request.getDueDate())
                .userEmail(userEmail)
                .build();
        return mapToResponse(todoRepository.save(todo));
    }

    public TodoResponse updateTodo(Long id, TodoRequest request, String userEmail) {
        Todo todo = todoRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));

        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        if (request.getPriority() != null) {
            todo.setPriority(request.getPriority());
        }
        todo.setDueDate(request.getDueDate());

        return mapToResponse(todoRepository.save(todo));
    }

    public TodoResponse toggleComplete(Long id, String userEmail) {
        Todo todo = todoRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));

        todo.setCompleted(!todo.isCompleted());
        return mapToResponse(todoRepository.save(todo));
    }

    public void deleteTodo(Long id, String userEmail) {
        Todo todo = todoRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));
        todoRepository.delete(todo);
    }

    // Called by Settings → Clear all data
    public void deleteAllTodos(String userEmail) {
        todoRepository.deleteAllByUserEmail(userEmail);
    }

    private TodoResponse mapToResponse(Todo todo) {
        return TodoResponse.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .completed(todo.isCompleted())
                .priority(todo.getPriority())
                .dueDate(todo.getDueDate())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();
    }
}