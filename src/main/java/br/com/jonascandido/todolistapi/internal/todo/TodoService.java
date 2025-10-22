package br.com.jonascandido.todolistapi.internal.todo;

import br.com.jonascandido.todolistapi.internal.todo.Todo;
import br.com.jonascandido.todolistapi.internal.todostatus.TodoStatus;
import br.com.jonascandido.todolistapi.internal.todo.TodoRepository;
import br.com.jonascandido.todolistapi.internal.todostatus.TodoStatusRepository;
import br.com.jonascandido.todolistapi.internal.user.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoStatusRepository todoStatusRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository,
                       TodoStatusRepository todoStatusRepository,
                       UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.todoStatusRepository = todoStatusRepository;
        this.userRepository = userRepository;
    }

    public Todo createTodo(Todo todo) {
    
        if (!userRepository.existsById(todo.getUser().getId())) {
            throw new IllegalArgumentException("Invalid User");
        }
        Optional<TodoStatus> statusOpt = todoStatusRepository.findByName(todo.getStatus().getName());
        if (statusOpt.isEmpty()) {
            throw new IllegalArgumentException("Invalid Status");
        }

        todo.setStatus(statusOpt.get());
        return todoRepository.save(todo);
    }

    public Todo update(Integer id, Todo updatedTodo) {
        Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Todo not found"));

        todo.setTitle(updatedTodo.getTitle());
        todo.setDescription(updatedTodo.getDescription());
        TodoStatus status = todoStatusRepository.findByName(updatedTodo.getStatus().getName())
            .orElseThrow(() -> new EntityNotFoundException("TodoStatus not found"));
        todo.setStatus(status);

        return todoRepository.save(todo);
    }

    public void delete(Integer id) {
        Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Todo not found"));
        todoRepository.delete(todo);
    }

    public Page<Todo> getTodos(int page, int limit) {
        return todoRepository.findAll(PageRequest.of(page - 1, limit));
    }
}
