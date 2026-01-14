package br.com.jonascandido.todolistapi.internal.todo.dto;

public record CreateTodoRequest(
        String title,
        String description,
        String status
) {}
