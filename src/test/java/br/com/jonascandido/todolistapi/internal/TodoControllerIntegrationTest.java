package br.com.jonascandido.todolistapi.internal;

import br.com.jonascandido.todolistapi.internal.todo.*;
import br.com.jonascandido.todolistapi.internal.user.*;
import br.com.jonascandido.todolistapi.internal.todostatus.*;

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
    
    @BeforeEach
    void cleanDatabase() {
        todoRepository.deleteAll();
    }

    @Test
    void shouldCreateAndReturnTodo() throws Exception {
        String todoJson = """
        {
          "title": "Complete API",
          "description": "Complete Spring Boot API",
          "status": {
              "name": Pending
          }
        }
        """;

        mockMvc.perform(post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoJson))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").exists())
               .andExpect(jsonPath("$.title").value("Complete API"))
               .andExpect(jsonPath("$.description").value("Complete Spring Boot API"))
               .andExpect(jsonPath("$.status.name").value("Pending"));
               // Expect user/principal ??
    }
}
