package br.com.jonascandido.todolistapi.internal.todo.dto;

import br.com.jonascandido.todolistapi.internal.todo.*;

public record TodoDTO(
        Integer id,
        String title,
        String description
) {
    public static TodoDTO fromEntity(Todo todo) {
        return new TodoDTO(todo.getId(), todo.getTitle(), todo.getDescription());
    }
}
