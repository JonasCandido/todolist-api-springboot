package br.com.jonascandido.todolistapi.internal.auth;

import br.com.jonascandido.todolistapi.internal.user.UserService;
import br.com.jonascandido.todolistapi.config.JwtUtil;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        if (userService.validateUser(request.email(), request.password())) {
            String token = jwtUtil.generateToken(request.email());
            return ResponseEntity.ok(new JwtResponse(token));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials"));
        }
    }

    public record JwtResponse(String token) {}
    public record LoginRequest(String email, String password) {}
}

