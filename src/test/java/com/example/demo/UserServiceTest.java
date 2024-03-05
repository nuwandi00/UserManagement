package com.example.demo;

import com.example.demo.user.User;
import com.example.demo.user.UserNotFoundException;
import com.example.demo.user.UserRepository;
import com.example.demo.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1);
        testUser.setFirstName("John");
        testUser.setLastName("Doe");
    }

    @Test
    void testListAll() {
        List<User> userList = new ArrayList<>();
        userList.add(testUser);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.listAll();

        assertEquals(1, result.size());
        assertEquals(testUser.getFirstName(), result.get(0).getFirstName());
        assertEquals(testUser.getLastName(), result.get(0).getLastName());
    }

    @Test
    void testSave() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User savedUser = userService.save(testUser);

        assertNotNull(savedUser);
        assertEquals(testUser.getId(), savedUser.getId());
        assertEquals(testUser.getFirstName(), savedUser.getFirstName());
        assertEquals(testUser.getLastName(), savedUser.getLastName());
    }

    @Test
    void testGet() throws UserNotFoundException {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        User result = userService.get(1);

        assertNotNull(result);
        assertEquals(testUser.getFirstName(), result.getFirstName());
        assertEquals(testUser.getLastName(), result.getLastName());
    }

    @Test
    void testDelete() {
        when(userRepository.countById(1)).thenReturn(1L);

        assertDoesNotThrow(() -> userService.delete(1));
    }

    @Test
    void testListAllSortedByFirstName() {
        List<User> userList = new ArrayList<>();
        userList.add(testUser);

        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.listAllSortedByFirstName();

        assertEquals(1, result.size());
        assertEquals(testUser.getFirstName(), result.get(0).getFirstName());
        assertEquals(testUser.getLastName(), result.get(0).getLastName());
    }

    @Test
    void testGet_UserNotFoundException() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.get(1));

        assertEquals("Could not find any users with ID 1", exception.getMessage());
    }

    @Test
    void testDelete_UserNotFoundException() {
        when(userRepository.countById(1)).thenReturn(0L);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> userService.delete(1));

        assertEquals("Could not find any users with ID 1", exception.getMessage());
    }


}
