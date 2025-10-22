package br.com.jonascandido.todolistapi.internal.user;

import br.com.jonascandido.todolistapi.internal.user.User;
import br.com.jonascandido.todolistapi.internal.todostatus.TodoStatus;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User addUser(User user) {

        // Validate unique email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Failed to create user");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        return userRepository.save(user);
    }

    public boolean validateUser(String email, String rawPassword) {
        return userRepository.findByEmail(email)
                .map(user -> passwordEncoder.matches(rawPassword, user.getPassword()))
                .orElse(false);
    }
}
