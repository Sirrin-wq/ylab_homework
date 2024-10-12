package ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.ylab.habittracker.entity.Habit;
import ru.ylab.habittracker.entity.User;
import ru.ylab.habittracker.security.UserSecurityManager;
import ru.ylab.habittracker.service.HabitService;
import ru.ylab.habittracker.service.UserService;
import ru.ylab.habittracker.ui.HabitTrackerDisplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HabitTrackerDisplayTest {
    private UserService userService;
    private HabitService habitService;
    private UserSecurityManager securityManager;
    private HabitTrackerDisplay habitTrackerDisplay;
    private Scanner scanner;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        habitService = mock(HabitService.class);
        securityManager = mock(UserSecurityManager.class);
        habitTrackerDisplay = new HabitTrackerDisplay(userService, habitService, securityManager);
        scanner = new Scanner(System.in);
    }

    @Test
    @DisplayName("Should register a user successfully")
    void testRegisterUser() {
        String name = "Test User";
        String email = "test@example.com";
        String password = "password";

        doNothing().when(securityManager).registerUser(name, email, password);

        habitTrackerDisplay.registerUser(new Scanner("Test User\ntest@example.com\npassword\n"));

        verify(securityManager).registerUser(name, email, password);
    }

    @Test
    @DisplayName("Should add a habit successfully")
    void testAddHabit() {
        String habitName = "Exercise";
        String habitDescription = "Daily morning exercise";
        String frequency = "Daily";
        Habit newHabit = new Habit(habitName, habitDescription, frequency);
        UserSecurityManager.currentUser = new User("Test User", "test@example.com", "password");

        habitTrackerDisplay.addHabit(new Scanner("Exercise\nDaily morning exercise\n1\n"));

        verify(habitService).addHabit(UserSecurityManager.currentUser.getEmail(), newHabit);
    }

    @Test
    @DisplayName("Should view habits successfully")
    void testViewHabits() {
        UserSecurityManager.currentUser = new User("Test User", "test@example.com", "password");
        List<Habit> habits = new ArrayList<>();
        habits.add(new Habit("Exercise", "Daily morning exercise", "Daily"));
        when(habitService.getUserHabits(UserSecurityManager.currentUser.getEmail())).thenReturn(habits);

        habitTrackerDisplay.viewHabits();

        verify(habitService).getUserHabits(UserSecurityManager.currentUser.getEmail());
    }

    @Test
    @DisplayName("Should update a habit successfully")
    void testUpdateHabit() {
        String habitName = "Exercise";
        Habit existingHabit = new Habit(habitName, "Daily morning exercise", "Daily");
        UserSecurityManager.currentUser = new User("Test User", "test@example.com", "password");
        when(habitService.getHabitByUserEmail(UserSecurityManager.currentUser.getEmail(), habitName)).thenReturn(existingHabit);

        habitTrackerDisplay.updateHabit(new Scanner("Exercise\nNew description\n"));

        verify(habitService).updateHabit(UserSecurityManager.currentUser.getEmail(), existingHabit);
        assertEquals("New description", existingHabit.getDescription());
    }

    @Test
    @DisplayName("Should delete a habit successfully")
    void testDeleteHabit() {
        String habitName = "Exercise";
        UserSecurityManager.currentUser = new User("Test User", "test@example.com", "password");

        habitTrackerDisplay.deleteHabit(new Scanner("Exercise\n"));

        verify(habitService).deleteHabit(UserSecurityManager.currentUser.getEmail(), habitName);
    }
}

