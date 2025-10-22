package br.com.jonascandido.todolistapi.internal;

import br.com.jonascandido.todolistapi.internal.user.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Arrays;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    // Unit tests

    @Test
    void testAddUser_Success() {
        User user = new User("Charlie", "charlie@example.com", "123456");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User saved = userService.addUser(user);

        assertEquals(user.getEmail(), saved.getEmail());
        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void testAddUser_EmailAlreadyExists() {
        User user = new User("David", "david@example.com", "123456");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.addUser(user);
        });

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository, never()).save(any());
    }
}
