package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.ylab.habittracker.entity.User;
import ru.ylab.habittracker.repository.HabitRepository;
import ru.ylab.habittracker.repository.impl.UserRepositoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryImplTest {
    private UserRepositoryImpl userRepository;
    private HabitRepository habitRepository;

    @BeforeEach
    void setUp() {
        habitRepository = Mockito.mock(HabitRepository.class);
        userRepository = new UserRepositoryImpl(habitRepository);
    }

    @Test
    @DisplayName("Should find all users successfully")
    void testFindAllUsers() {
        User user1 = new User("User One", "user1@example.com", "password");
        User user2 = new User("User Two", "user2@example.com", "password");
        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    @DisplayName("Should return an empty list when no users are found")
    void testFindAllUsersEmpty() {
        List<User> users = userRepository.findAll();
        assertTrue(users.isEmpty());
    }

    @Test
    @DisplayName("Should save a user successfully")
    void testSaveUser() {
        User user = new User("User", "user@example.com", "password");
        userRepository.save(user);

        User foundUser = userRepository.findByEmail("user@example.com");
        assertEquals("User", foundUser.getName());
    }

    @Test
    @DisplayName("Should throw an exception when saving a duplicate user")
    void testSaveDuplicateUser() {
        User user = new User("User", "user@example.com", "password");
        userRepository.save(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userRepository.save(user);
        });
        assertEquals("User with this email already exists: user@example.com", exception.getMessage());
    }

    @Test
    @DisplayName("Should find a user by email successfully")
    void testFindByEmail() {
        User user = new User("User", "user@example.com", "password");
        userRepository.save(user);

        User foundUser = userRepository.findByEmail("user@example.com");
        assertEquals("User", foundUser.getName());
    }

    @Test
    @DisplayName("Should throw an exception when user is not found by email")
    void testFindByEmailNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userRepository.findByEmail("nonexistent@example.com");
        });
        assertEquals("User not found with email: nonexistent@example.com", exception.getMessage());
    }

    @Test
    @DisplayName("Should delete a user by email successfully")
    void testDeleteByEmail() {
        User user = new User("User", "user@example.com", "password");
        userRepository.save(user);
        userRepository.deleteByEmail("user@example.com");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userRepository.findByEmail("user@example.com");
        });
        assertEquals("User not found with email: user@example.com", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw an exception when deleting a user that does not exist")
    void testDeleteByEmailNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userRepository.deleteByEmail("nonexistent@example.com");
        });
        assertEquals("User with this email does not exist: nonexistent@example.com", exception.getMessage());
    }

    @Test
    @DisplayName("Should delete a user and their habits successfully")
    void testDeleteByEmailWithHabitDeletion() {
        User user = new User("User", "user@example.com", "password");
        userRepository.save(user);

        Mockito.doNothing().when(habitRepository).deleteAllUserHabits("user@example.com");

        userRepository.deleteByEmail("user@example.com");

        Mockito.verify(habitRepository).deleteAllUserHabits("user@example.com");
    }
}