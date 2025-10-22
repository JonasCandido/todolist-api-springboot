package br.com.jonascandido.todolistapi.internal.user;

import br.com.jonascandido.todolistapi.internal.user.User;
import br.com.jonascandido.todolistapi.internal.todostatus.TodoStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {

        // Validate unique email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        return userRepository.save(user);
    }
}
