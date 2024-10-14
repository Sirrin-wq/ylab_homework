package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.ylab.habittracker.entity.Habit;
import ru.ylab.habittracker.repository.HabitRepository;
import ru.ylab.habittracker.service.impl.HabitServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HabitServiceImplTest {
    private HabitServiceImpl habitService;
    private HabitRepository habitRepository;

    @BeforeEach
    void setUp() {
        habitRepository = Mockito.mock(HabitRepository.class);
        habitService = new HabitServiceImpl(habitRepository);
    }

    @Test
    @DisplayName("Should retrieve user habits successfully")
    void testGetUserHabits() {
        String userEmail = "user@example.com";
        List<Habit> habits = new ArrayList<>();
        habits.add(new Habit("Exercise", "Daily workout", "Daily"));
        habits.add(new Habit("Read", "Read a book", "Daily"));

        Mockito.when(habitRepository.findUserHabits(userEmail)).thenReturn(habits);

        List<Habit> retrievedHabits = habitService.getUserHabits(userEmail);
        assertEquals(2, retrievedHabits.size());
        assertEquals("Exercise", retrievedHabits.get(0).getName());
    }

    @Test
    @DisplayName("Should retrieve a specific habit by user email")
    void testGetHabitByUserEmail() {
        String userEmail = "user@example.com";
        Habit habit = new Habit("Exercise", "Daily workout", "Daily");

        Mockito.when(habitRepository.findHabitByUserEmail(userEmail, "Exercise")).thenReturn(habit);

        Habit retrievedHabit = habitService.getHabitByUserEmail(userEmail, "Exercise");
        assertEquals("Exercise", retrievedHabit.getName());
    }

    @Test
    @DisplayName("Should add a new habit successfully")
    void testAddHabit() {
        String userEmail = "user@example.com";
        Habit habit = new Habit("Exercise", "Daily workout", "Daily");

        habitService.addHabit(userEmail, habit);
        Mockito.verify(habitRepository).addHabit(userEmail, habit);
    }

    @Test
    @DisplayName("Should update an existing habit successfully")
    void testUpdateHabit() {
        String userEmail = "user@example.com";
        Habit existingHabit = new Habit("Exercise", "Daily workout", "Daily");
        Habit updatedHabit = new Habit("Exercise", "Updated description", "Daily");

        Mockito.when(habitRepository.findHabitByUserEmail(userEmail, "Exercise")).thenReturn(existingHabit);

        habitService.updateHabit(userEmail, updatedHabit);
        Mockito.verify(habitRepository).updateHabit(userEmail, updatedHabit);
    }

    @Test
    @DisplayName("Should delete an existing habit successfully")
    void testDeleteHabit() {
        String userEmail = "user@example.com";
        Habit existingHabit = new Habit("Exercise", "Daily workout", "Daily");

        Mockito.when(habitRepository.findHabitByUserEmail(userEmail, "Exercise")).thenReturn(existingHabit);

        habitService.deleteHabit(userEmail, "Exercise");
        Mockito.verify(habitRepository).deleteHabit(userEmail, "Exercise");
    }

    @Test
    @DisplayName("Should delete all habits for a user successfully")
    void testDeleteAllUserHabits() {
        String userEmail = "user@example.com";

        habitService.deleteAllUserHabits(userEmail);
        Mockito.verify(habitRepository).deleteAllUserHabits(userEmail);
    }

    @Test
    @DisplayName("Should add a timestamp when marking an existing habit as completed")
    void testMarkHabitCompleted() {
        String userEmail = "user@example.com";
        Habit existingHabit = new Habit("Exercise", "Daily workout", "Daily");

        Mockito.when(habitRepository.findHabitByUserEmail(userEmail, "Exercise")).thenReturn(existingHabit);

        habitService.markHabitCompleted(userEmail, "Exercise");

        assertFalse(existingHabit.getTimestamps().isEmpty(), "Timestamps list should not be empty after marking as completed");
        long lastTimestamp = existingHabit.getTimestamps().get(existingHabit.getTimestamps().size() - 1);
        long currentTime = System.currentTimeMillis();

        assertTrue(Math.abs(currentTime - lastTimestamp) < 1000, "The last timestamp should be close to the current time");
    }


    @Test
    @DisplayName("Should retrieve habit statistics within a time range")
    void testGetHabitStatistics() {
        String userEmail = "user@example.com";
        String habitName = "Exercise";
        long startTime = 1000L;
        long endTime = 2000L;
        Habit existingHabit = new Habit(habitName, "Daily workout", "Daily");
        existingHabit.addTimestamp(500L);
        existingHabit.addTimestamp(1500L);
        existingHabit.addTimestamp(2500L);

        Mockito.when(habitRepository.findHabitByUserEmail(userEmail, habitName)).thenReturn(existingHabit);

        List<Long> statistics = habitService.getHabitStatistics(userEmail, habitName, startTime, endTime);
        assertEquals(1, statistics.size());
        assertEquals(1500L, statistics.get(0));
    }

}
