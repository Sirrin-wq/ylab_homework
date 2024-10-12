package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.ylab.habittracker.entity.User;
import ru.ylab.habittracker.repository.UserRepository;
import ru.ylab.habittracker.service.impl.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {
    private UserServiceImpl userService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    @DisplayName("Should retrieve all users successfully")
    void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("User One", "user1@example.com", "password1"));
        users.add(new User("User Two", "user2@example.com", "password2"));

        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> retrievedUsers = userService.getAll();
        assertEquals(2, retrievedUsers.size());
        assertEquals("User One", retrievedUsers.get(0).getName());
    }

    @Test
    @DisplayName("Should retrieve a user by email successfully")
    void testGetUserByEmail() {
        String email = "user@example.com";
        User user = new User("User Name", email, "password");

        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);

        User retrievedUser = userService.getByEmail(email);
        assertEquals("User Name", retrievedUser.getName());
    }

    @Test
    @DisplayName("Should add a new user successfully")
    void testAddUser() {
        String email = "newuser@example.com";
        String password = "newpassword";
        String name = "New User";

        userService.addUser(name, email, password);
        Mockito.verify(userRepository).save(Mockito.argThat(user ->
                user.getEmail().equals(email) &&
                        user.getPassword().equals(password) &&
                        user.getName().equals(name)
        ));
    }

    @Test
    @DisplayName("Should update an existing user successfully")
    void testUpdateUser() {
        String email = "user@example.com";
        User existingUser = new User(email, "oldpassword", "Old Name");

        Mockito.when(userRepository.findByEmail(email)).thenReturn(existingUser);

        String newName = "Updated Name";
        String newPassword = "newpassword";
        userService.updateUser(email, newName, newPassword);

        assertEquals(newName, existingUser.getName());
        assertEquals(newPassword, existingUser.getPassword());
    }

    @Test
    @DisplayName("Should delete a user successfully")
    void testDeleteUser() {
        String email = "user@example.com";

        userService.deleteUser(email);
        Mockito.verify(userRepository).deleteByEmail(email);
    }

    @Test
    @DisplayName("Should reset a user's password successfully")
    void testResetPassword() {
        String email = "user@example.com";
        User existingUser = new User(email, "oldpassword", "User Name");

        Mockito.when(userRepository.findByEmail(email)).thenReturn(existingUser);

        String newPassword = "newpassword";
        userService.resetPassword(email, newPassword);

        assertEquals(newPassword, existingUser.getPassword());
    }
}

