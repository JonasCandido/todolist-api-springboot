package br.com.jonascandido.todolistapi.internal.todo;

import br.com.jonascandido.todolistapi.internal.user.*;
import br.com.jonascandido.todolistapi.internal.todostatus.*;

import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
public class TodoController {

    private final TodoService todoService;
    private final TodoStatusRepository todoStatusRepository;
    
    public TodoController(TodoService todoService, TodoStatusRepository todoStatusRepository) {
        this.todoService = todoService;
        this.todoStatusRepository = todoStatusRepository;
    }
}
 
