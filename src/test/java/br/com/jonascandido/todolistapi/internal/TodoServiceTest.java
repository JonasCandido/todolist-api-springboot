package br.com.jonascandido.todolistapi.internal;

import br.com.jonascandido.todolistapi.internal.todo.*;
import br.com.jonascandido.todolistapi.internal.todostatus.*;
import br.com.jonascandido.todolistapi.internal.user.*;

import org.springframework.util.ReflectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import jakarta.persistence.EntityNotFoundException; 

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    private TodoRepository todoRepository;
    private UserRepository userRepository;
    private TodoStatusRepository todoStatusRepository;
    private TodoService todoService;

    @BeforeEach
    void setUp() {
        todoStatusRepository = mock(TodoStatusRepository.class);
    	todoRepository = mock(TodoRepository.class);
        userRepository = mock(UserRepository.class);
        todoService = new TodoService(todoRepository, todoStatusRepository, userRepository);
    }

    @Test
    void testAddTodo_Success() {
    	User user = new User("Charlie", "charlie@example.com", "123456");
    	TodoStatus pendingStatus = new TodoStatus("Pending");
    	Todo todo = new Todo("Laundry", "Do laundry", user, pendingStatus);

    	when(userRepository.existsById(user.getId())).thenReturn(true);
    
    	when(todoStatusRepository.findByName(pendingStatus.getName()))
        	.thenReturn(Optional.of(pendingStatus));
    
    	when(todoRepository.save(todo)).thenReturn(todo);

    	Todo savedTodo = todoService.createTodo(todo);

    	assertEquals(todo.getTitle(), savedTodo.getTitle());
    	assertEquals(user, savedTodo.getUser());
    	assertEquals(pendingStatus, savedTodo.getStatus());

    	verify(userRepository).existsById(user.getId());
    	verify(todoStatusRepository).findByName(pendingStatus.getName());
    	verify(todoRepository).save(todo);
   }

    @Test
    void testDeleteTodo_Success() {
        Todo todo = new Todo("Laundry", "Do laundry", new User("Charlie", "charlie@example.com", "123456"), new TodoStatus("Pending"));
        when(todoRepository.findById(1)).thenReturn(Optional.of(todo));

        todoService.delete(1);

        verify(todoRepository, times(1)).delete(todo);
    }

    @Test
    void testDeleteTodo_TodoNotFound() {
        when(todoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> todoService.delete(1));

        verify(todoRepository, never()).delete(any());
    }

    
   @Test
   void testUpdateTodo_Success(){
       User user = new User("Charlie", "charlie@example.com", "123456");
       TodoStatus pendingStatus = new TodoStatus("Pending");
       Todo todo = new Todo("Laundry", "Do laundry", user, pendingStatus);

       setId(todo, 1);

       when(userRepository.existsById(user.getId())).thenReturn(true);
    
       when(todoStatusRepository.findByName(pendingStatus.getName()))
        	.thenReturn(Optional.of(pendingStatus));

       when(todoRepository.save(any(Todo.class))).thenAnswer(i -> i.getArgument(0));

       
       TodoStatus completedStatus = new TodoStatus("Completed");
       Todo updated = new Todo("Laundry", "Do laundry", user, completedStatus);

       when(todoStatusRepository.findByName(completedStatus.getName()))
        	.thenReturn(Optional.of(completedStatus));

       when(todoRepository.findById(1)).thenReturn(Optional.of(todo));

       Todo result = todoService.update(1, updated);

       assertEquals(updated.getTitle(), result.getTitle());
       assertEquals(user, result.getUser());
   }

    @Test
    void testUpdateTodo_NotFound() {
        User user = new User("Charlie", "charlie@example.com", "123456");
        TodoStatus completedStatus = new TodoStatus("Completed");
        Todo updated = new Todo("Laundry", "Do laundry", user, completedStatus);

        when(todoRepository.findById(99)).thenReturn(Optional.empty());
        
        assertThrows(EntityNotFoundException.class, () -> todoService.update(99, updated));

        verify(todoRepository, never()).save(any(Todo.class));
    }

    @Test
    void testUpdateTodo_StatusNotFound() {
        User user = new User("Charlie", "charlie@example.com", "123456");
        TodoStatus invalidStatus = new TodoStatus("Nonexistent");
        Todo todo = new Todo("Laundry", "Do laundry", user, invalidStatus);

        when(todoRepository.findById(1)).thenReturn(Optional.of(todo));
        
        when(todoStatusRepository.findByName(invalidStatus.getName()))
            .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> todoService.update(1, todo));

        verify(todoRepository, never()).save(any(Todo.class));
    }


    @Test
    void shouldReturnPaginatedTodos() throws Exception {
        User user  = new User("Charlie", "charlie@example.com", "123456");
        TodoStatus pendingStatus = new TodoStatus("Pending");
        TodoStatus completedStatus = new TodoStatus("Completed");
        
        Todo todo1 = new Todo("Buy groceries", "Buy milk, eggs, bread", user, pendingStatus);
        Todo todo2 = new Todo("Pay bills", "Pay electricity and water bills", user, completedStatus);

        setId(todo1, 1);
        setId(todo2, 2);

        List<Todo> todos = Arrays.asList(todo1, todo2);
        Page<Todo> todosPage = new PageImpl<>(todos);

        when(todoRepository.findAll(PageRequest.of(0, 10))).thenReturn(todosPage);

        Page<Todo> result = todoService.getTodos(1, 10);

        assertEquals(2, result.getContent().size());
        assertEquals("Buy groceries", result.getContent().get(0).getTitle());
        assertEquals("Pay bills", result.getContent().get(1).getTitle());

        verify(todoRepository, times(1)).findAll(PageRequest.of(0, 10));
    }
    
    private void setId(Todo todo, Integer id) {
        Field idField = ReflectionUtils.findField(Todo.class, "id");
        ReflectionUtils.makeAccessible(idField);
        ReflectionUtils.setField(idField, todo, id);
    }
}
