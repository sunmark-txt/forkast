package com.forkast.service;

import com.forkast.exception.DuplicateResourceException;
import com.forkast.exception.ResourceNotFoundException;
import com.forkast.model.User;
import com.forkast.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
    }

    @Test
    void register_WithValidUser_ShouldSucceed() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User registeredUser = authService.register(testUser);

        assertNotNull(registeredUser);
        assertEquals(testUser.getEmail(), registeredUser.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_WithExistingEmail_ShouldThrowException() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        assertThrows(DuplicateResourceException.class, () -> authService.register(testUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_WithValidCredentials_ShouldSucceed() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(testUser.getPassword(), testUser.getPassword())).thenReturn(true);

        User loggedInUser = authService.login(testUser.getEmail(), testUser.getPassword());

        assertNotNull(loggedInUser);
        assertEquals(testUser.getEmail(), loggedInUser.getEmail());
    }

    @Test
    void login_WithInvalidEmail_ShouldThrowException() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, 
            () -> authService.login(testUser.getEmail(), testUser.getPassword()));
    }

    @Test
    void login_WithInvalidPassword_ShouldThrowException() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(testUser.getPassword(), testUser.getPassword())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, 
            () -> authService.login(testUser.getEmail(), "wrongPassword"));
    }
} 