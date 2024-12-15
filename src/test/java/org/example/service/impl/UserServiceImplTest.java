package org.example.service.impl;

import org.example.model.User;
import org.example.repository.UserEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserEntityRepository userRepository;

    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void save() {
        User user = new User(UUID.randomUUID(), "Test User", "test@example.com");
        when(userRepository.save(user)).thenReturn(user); // Возвращаем тот же User

        User savedUser = userService.save(user);
        assertEquals(user, savedUser);
        verify(userRepository).save(user);
    }

    @Test
    void findById_userFound() {
        UUID testUUID = UUID.randomUUID();
        User mockUser = new User(testUUID, "Test User", "test@example.com");
        when(userRepository.findById(testUUID)).thenReturn(mockUser);

        User foundUser = userService.findById(testUUID);
        assertEquals(mockUser, foundUser);
        verify(userRepository).findById(testUUID);
    }

    @Test
    void findById_userNotFound() {
        UUID testUUID = UUID.randomUUID();
        when(userRepository.findById(testUUID)).thenReturn(null);

        User foundUser = userService.findById(testUUID);
        assertNull(foundUser);
        verify(userRepository).findById(testUUID);
    }

    @Test
    void findAll() {
        List<User> mockUsers = List.of(
                new User(UUID.randomUUID(), "User 1", "user1@example.com"),
                new User(UUID.randomUUID(), "User 2", "user2@example.com")
        );
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> allUsers = userService.findAll();
        assertEquals(mockUsers, allUsers);
        verify(userRepository).findAll();
    }

    @Test
    void createTable() {
        userService.createTable();
        verify(userRepository).createTable();
    }

    @Test
    void deleteById_userExists() {
        UUID testUUID = UUID.randomUUID();
        when(userRepository.deleteById(testUUID)).thenReturn(true);
        assertTrue(userService.deleteById(testUUID));
        verify(userRepository).deleteById(testUUID);
    }

    @Test
    void deleteById_userNotExists() {
        UUID testUUID = UUID.randomUUID();
        when(userRepository.deleteById(testUUID)).thenReturn(false);
        assertFalse(userService.deleteById(testUUID));
        verify(userRepository).deleteById(testUUID);
    }
}