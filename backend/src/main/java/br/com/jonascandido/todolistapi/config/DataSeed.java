package br.com.jonascandido.todolistapi.config;

import br.com.jonascandido.todolistapi.internal.todo.Todo;
import br.com.jonascandido.todolistapi.internal.todo.TodoRepository;
import br.com.jonascandido.todolistapi.internal.todostatus.TodoStatus;
import br.com.jonascandido.todolistapi.internal.todostatus.TodoStatusRepository;
import br.com.jonascandido.todolistapi.internal.user.User;
import br.com.jonascandido.todolistapi.internal.user.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class DataSeed {

    @Bean
    CommandLineRunner init(UserRepository userRepository,
                           TodoStatusRepository todoStatusRepository,
                           TodoRepository todoRepository,
                           PasswordEncoder passwordEncoder) {
        return args -> {

            todoRepository.deleteAll();
            todoStatusRepository.deleteAll();
            userRepository.deleteAll();
            
            // --- USERS ---
            if (userRepository.count() == 0) {
                User admin = new User(
                        "Admin",
                        "admin@mysite.com",
                        passwordEncoder.encode("admin123"),
                        true
                );
                userRepository.save(admin);
            }

            // --- STATUS ---
            if (todoStatusRepository.count() == 0) {
                TodoStatus pending = new TodoStatus("Pending");
                TodoStatus inProgress = new TodoStatus("In Progress");
                TodoStatus completed = new TodoStatus("Completed");
                todoStatusRepository.save(pending);
                todoStatusRepository.save(inProgress);
                todoStatusRepository.save(completed);
            }

            // --- TODOS ---
            if (todoRepository.count() == 0) {
                User admin = userRepository.findByEmail("admin@mysite.com").orElseThrow();
                TodoStatus pending = todoStatusRepository.findByName("Pending").orElseThrow();
                TodoStatus inProgress = todoStatusRepository.findByName("In Progress").orElseThrow();
                TodoStatus completed = todoStatusRepository.findByName("Completed").orElseThrow();

                todoRepository.save(new Todo(
                        "Prepare project",
                        "Set up project structure and dependencies",
                        admin,
                        pending
                ));
                todoRepository.save(new Todo(
                        "Write documentation",
                        "Write initial project documentation",
                        admin,
                        inProgress
                ));
                todoRepository.save(new Todo(
                        "Release version 1",
                        "Deploy first version to production",
                        admin,
                        completed
                ));

                // Add more todos for testing purposes 
                for (int i = 1; i <= 30; i++) {
                    TodoStatus status;
                    if (i % 3 == 0) status = completed;
                    else if (i % 3 == 1) status = pending;
                    else status = inProgress;

                    todoRepository.save(new Todo(
                            "Todo #" + i,
                            "Description for todo #" + i,
                            admin,
                            status
                    ));
                }
            }
        };
    }
}
