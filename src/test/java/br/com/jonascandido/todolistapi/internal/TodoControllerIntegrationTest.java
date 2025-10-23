package br.com.jonascandido.todolistapi.internal;

import br.com.jonascandido.todolistapi.internal.todo.*;
import br.com.jonascandido.todolistapi.internal.user.*;
import br.com.jonascandido.todolistapi.internal.todostatus.*;
import br.com.jonascandido.todolistapi.config.JwtUtil;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TodoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoStatusRepository todoStatusRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private User testUser;
    private String jwtToken;

    @BeforeEach
    void setup() {
        todoRepository.deleteAll();
        userRepository.deleteAll();
        todoStatusRepository.deleteAll();

        testUser = new User("Jonas", "jonas@example.com", "$2a$10$T70FXpYfDNlKl5.UaXbbieLbrjPxsMfzdTaJeLCS/FwdRtPf3xs3e", true);
        userRepository.save(testUser);

        jwtToken = jwtUtil.generateToken(testUser.getEmail());

        TodoStatus pendingStatus = new TodoStatus("Pending");
        todoStatusRepository.save(pendingStatus);
    }

    @Test
    void shouldCreateAndReturnTodo() throws Exception {
        String todoJson = """
        {
          "title": "Complete API",
          "description": "Complete Spring Boot API",
          "status": "Pending"
        }
        """;

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwtToken)
                        .content(todoJson))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").exists())
               .andExpect(jsonPath("$.title").value("Complete API"))
               .andExpect(jsonPath("$.description").value("Complete Spring Boot API"))
               .andExpect(jsonPath("$.status").value("Pending"));
    }

    @Test
    void shouldGetTodos() throws Exception {
        TodoStatus status = todoStatusRepository.findByName("Pending").get();

        Todo todo = new Todo("Complete API", "Complete Spring Boot API", testUser, status);
        todoRepository.save(todo);

        mockMvc.perform(get("/todos?page=1&limit=10")
                        .header("Authorization", "Bearer " + jwtToken))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.data[0].id").value(todo.getId()))
               .andExpect(jsonPath("$.data[0].title").value("Complete API"))
               .andExpect(jsonPath("$.data[0].description").value("Complete Spring Boot API"))
               .andExpect(jsonPath("$.data[0].status").value("Pending"))
               .andExpect(jsonPath("$.page").value(1))
               .andExpect(jsonPath("$.limit").value(10))
               .andExpect(jsonPath("$.total").value(1));
    }

    @Test
    void shouldUpdateTodoSuccessfully() throws Exception {
        TodoStatus status = todoStatusRepository.findByName("Pending").get();

        Todo todo = new Todo("Complete API", "Complete Spring Boot API", testUser, status);
        todoRepository.save(todo);

        String updatedTodoJson = """
            {
                "title": "Complete API Updated",
                "description": "Updated description",
                "status": "Pending"
            }
        """;

            mockMvc.perform(put("/todos/" + todo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken)
                            .content(updatedTodoJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(todo.getId()))
            .andExpect(jsonPath("$.title").value("Complete API Updated"))
            .andExpect(jsonPath("$.description").value("Updated description"))
            .andExpect(jsonPath("$.status").value("Pending"));
    }

    @Test
    void shouldReturnForbiddenWhenUpdatingOthersTodo() throws Exception {
        User otherUser = new User("Other", "other@example.com", "$2a$10$T70FXpYfDNlKl5.UaXbbieLbrjPxsMfzdTaJeLCS/FwdRtPf3xs3e", true);
        userRepository.save(otherUser);

        TodoStatus status = todoStatusRepository.findByName("Pending").get();

        Todo othersTodo = new Todo("Other's Todo", "Do something", otherUser, status);
        todoRepository.save(othersTodo);

        String updatedTodoJson = """
            {
                "title": "Hack Attempt",
                "description": "Trying to update someone else's todo",
                "status": "Pending"
            }
        """;

            mockMvc.perform(put("/todos/" + othersTodo.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", "Bearer " + jwtToken)
                            .content(updatedTodoJson))
            .andExpect(status().isForbidden())
            .andExpect(jsonPath("$.message").value("Forbidden"));
    }

}
