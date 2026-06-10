package com.clerca.Backend.dto;

import com.clerca.Backend.entity.Todo.Priority;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TodoRequest {
    private String title;
    private String description;
    private Priority priority;
    private LocalDateTime dueDate;
}
