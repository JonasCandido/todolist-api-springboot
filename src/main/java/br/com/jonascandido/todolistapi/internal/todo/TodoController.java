package br.com.jonascandido.todolistapi.internal.todo;

import br.com.jonascandido.todolistapi.internal.todo.dto.CreateTodoRequest;
import br.com.jonascandido.todolistapi.internal.todo.dto.TodoDTO;
import br.com.jonascandido.todolistapi.internal.todo.dto.TodoPageResponse;
import br.com.jonascandido.todolistapi.internal.todostatus.TodoStatus;
import br.com.jonascandido.todolistapi.internal.todostatus.TodoStatusRepository;
import br.com.jonascandido.todolistapi.internal.user.User;
import br.com.jonascandido.todolistapi.internal.user.UserRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;
    private final TodoStatusRepository todoStatusRepository;
    private final UserRepository userRepository;

    public TodoController(TodoService todoService,
                          TodoStatusRepository todoStatusRepository,
                          UserRepository userRepository) {
        this.todoService = todoService;
        this.todoStatusRepository = todoStatusRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<TodoPageResponse> getTodos(@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int limit,
                                                     Principal principal) {
        String email = principal.getName();

        Page<Todo> todosPage = todoService.getTodos(email, page, limit);

        List<TodoDTO> todosList = todosPage.getContent()
                                           .stream()
                                           .map(TodoDTO::fromEntity)
                                           .collect(Collectors.toList());

        TodoPageResponse response = new TodoPageResponse(todosList,
                                                         page,
                                                         limit,
                                                         todosPage.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody CreateTodoRequest request,
                                        Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                 .body(java.util.Map.of("message", "Unauthorized"));
        }

        User user = userRepository.findByEmail(principal.getName())
                                  .orElseThrow(() -> new IllegalArgumentException("Invalid User"));

        Optional<TodoStatus> statusOpt = todoStatusRepository.findByName(request.status());
        if (statusOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .body(java.util.Map.of("message", "Invalid status"));
        }

        Todo todo = new Todo(request.title(), request.description(), user, statusOpt.get());
        Todo createdTodo = todoService.createTodo(todo);

        TodoDTO response = TodoDTO.fromEntity(createdTodo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> updateTodo(@PathVariable Integer id,
                                        @RequestBody Todo updatedTodo,
                                        Principal principal) {

        String email = principal.getName();
        Todo existingTodo = todoService.getTodoById(id);

        if (!existingTodo.getUser().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "Forbidden"));
        }

        Todo updated = todoService.update(id, updatedTodo);
        TodoDTO response = TodoDTO.fromEntity(updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Integer id, Principal principal) {
        String email = principal.getName();
        Todo todo = todoService.getTodoById(id);

        if (!todo.getUser().getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("message", "Forbidden"));
        }

        todoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
