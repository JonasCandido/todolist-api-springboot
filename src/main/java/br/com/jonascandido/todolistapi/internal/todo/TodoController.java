package br.com.jonascandido.todolistapi.internal.todo;

import br.com.jonascandido.todolistapi.internal.user.*;
import br.com.jonascandido.todolistapi.internal.todostatus.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.security.Principal;

@RestController
public class TodoController {

    private final TodoService todoService;
    private final TodoStatusRepository todoStatusRepository;
    
    public TodoController(TodoService todoService, TodoStatusRepository todoStatusRepository) {
        this.todoService = todoService;
        this.todoStatusRepository = todoStatusRepository;
    }

    @GetMapping("/todos")
    public ResponseEntity<Page<Todo>> getTodos(@RequestParam(defaultValue = "1") int page,
                                               @RequestParam(defaultValue = "10") int limit,
                                               Principal principal) { 
        
        String email = principal.getName();
        
        Page<Todo> todos = todoService.getTodos(email, page, limit);
        
        return ResponseEntity.ok(todos);
    }
}
 
