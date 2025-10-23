package br.com.jonascandido.todolistapi.internal.todo.dto;

import br.com.jonascandido.todolistapi.internal.todo.dto.TodoDTO;

import java.util.List;

public record TodoPageResponse(
        List<TodoDTO> data,
        int page,
        int limit,
        long total
) {}
