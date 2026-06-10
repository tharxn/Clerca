package com.clerca.Backend.service;

import com.clerca.Backend.dto.TodoRequest;
import com.clerca.Backend.dto.TodoResponse;
import com.clerca.Backend.entity.Todo;
import com.clerca.Backend.repository.TodoRepository;
import com.clerca.Backend.exception.ResourceNotFoundException;
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
                .stream().map(this::toResponse).toList();
    }

    public List<TodoResponse> getCompletedTodos(String userEmail) {
        return todoRepository.findByUserEmailAndCompletedTrue(userEmail)
                .stream().map(this::toResponse).toList();
    }

    public List<TodoResponse> getMissedTodos(String userEmail) {
        // Missed = not completed AND due date is in the past
        return todoRepository.findByUserEmailAndCompletedFalseAndDueDateBefore(userEmail, LocalDateTime.now())
                .stream().map(this::toResponse).toList();
    }

    public TodoResponse createTodo(TodoRequest request, String userEmail) {
        Todo todo = Todo.builder()
                .userEmail(userEmail)
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(request.getPriority() != null ? request.getPriority() : Todo.Priority.MEDIUM)
                .dueDate(request.getDueDate())
                .completed(false)
                .build();
        return toResponse(todoRepository.save(todo));
    }

    public TodoResponse updateTodo(Long id, TodoRequest request, String userEmail) {
        Todo todo = todoRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setPriority(request.getPriority());
        todo.setDueDate(request.getDueDate());
        return toResponse(todoRepository.save(todo));
    }

    public TodoResponse toggleComplete(Long id, String userEmail) {
        Todo todo = todoRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));
        todo.setCompleted(!todo.isCompleted());
        return toResponse(todoRepository.save(todo));
    }

    public void deleteTodo(Long id, String userEmail) {
        Todo todo = todoRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found"));
        todoRepository.delete(todo);
    }

    private TodoResponse toResponse(Todo t) {
        return TodoResponse.builder()
                .id(t.getId())
                .title(t.getTitle())
                .description(t.getDescription())
                .completed(t.isCompleted())
                .priority(t.getPriority())
                .dueDate(t.getDueDate())
                .createdAt(t.getCreatedAt())
                .updatedAt(t.getUpdatedAt())
                .build();
    }
}