package br.com.jonascandido.todolistapi.internal.user;

import br.com.jonascandido.todolistapi.config.JwtUtil;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User savedUser = userService.addUser(user);
            String token = jwtUtil.generateToken(savedUser.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED)
                .body(new TokenResponse(token));
        } catch (IllegalArgumentException e) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        }
    }
    public record TokenResponse(String token) {}
    
}
