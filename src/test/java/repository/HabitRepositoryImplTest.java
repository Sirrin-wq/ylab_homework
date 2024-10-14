package repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.ylab.habittracker.entity.Habit;
import ru.ylab.habittracker.repository.impl.HabitRepositoryImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HabitRepositoryImplTest {
    private HabitRepositoryImpl habitRepository;

    @BeforeEach
    void setUp() {
        habitRepository = new HabitRepositoryImpl();
    }

    @Test
    @DisplayName("Should add a habit successfully")
    void testAddHabit() {
        Habit habit = new Habit("Exercise", "Daily workout", "Daily");
        habitRepository.addHabit("user@example.com", habit);

        List<Habit> habits = habitRepository.findUserHabits("user@example.com");
        assertEquals(1, habits.size());
        assertEquals("Exercise", habits.get(0).getName());
    }

    @Test
    @DisplayName("Should throw an exception when adding a duplicate habit")
    void testAddDuplicateHabit() {
        Habit habit = new Habit("Exercise", "Daily workout", "Daily");
        habitRepository.addHabit("user@example.com", habit);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            habitRepository.addHabit("user@example.com", habit);
        });
        assertEquals("Habit already exists for this user: Exercise", exception.getMessage());
    }

    @Test
    @DisplayName("Should find all habits for a user")
    void testFindUserHabits() {
        Habit habit1 = new Habit("Exercise", "Daily workout", "Daily");
        Habit habit2 = new Habit("Read", "Read a book", "Daily");
        habitRepository.addHabit("user@example.com", habit1);
        habitRepository.addHabit("user@example.com", habit2);

        List<Habit> habits = habitRepository.findUserHabits("user@example.com");
        assertEquals(2, habits.size());
    }

    @Test
    @DisplayName("Should find a specific habit by user email")
    void testFindHabitByUserEmail() {
        Habit habit = new Habit("Exercise", "Daily workout", "Daily");
        habitRepository.addHabit("user@example.com", habit);

        Habit foundHabit = habitRepository.findHabitByUserEmail("user@example.com", "Exercise");
        assertEquals("Exercise", foundHabit.getName());
    }

    @Test
    @DisplayName("Should throw an exception when habit is not found by user email")
    void testFindHabitByUserEmailNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            habitRepository.findHabitByUserEmail("user@example.com", "NonExistentHabit");
        });
        assertEquals("Habit not found: NonExistentHabit", exception.getMessage());
    }

    @Test
    @DisplayName("Should update an existing habit successfully")
    void testUpdateHabit() {
        Habit habit = new Habit("Exercise", "Daily workout", "Daily");
        habitRepository.addHabit("user@example.com", habit);

        Habit updatedHabit = new Habit("Exercise", "Updated description", "Daily");
        updatedHabit.setCompleted(true);
        habitRepository.updateHabit("user@example.com", updatedHabit);

        Habit foundHabit = habitRepository.findHabitByUserEmail("user@example.com", "Exercise");
        assertEquals("Updated description", foundHabit.getDescription());
        assertTrue(foundHabit.isCompleted());
    }

    @Test
    @DisplayName("Should throw an exception when updating a non-existent habit")
    void testUpdateHabitNotFound() {
        Habit habit = new Habit("Exercise", "Daily workout", "Daily");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            habitRepository.updateHabit("user@example.com", habit);
        });
        assertEquals("No habits found for user: user@example.com", exception.getMessage());
    }

    @Test
    @DisplayName("Should delete a habit successfully")
    void testDeleteHabit() {
        Habit habit = new Habit("Exercise", "Daily workout", "Daily");
        habitRepository.addHabit("user@example.com", habit);
        habitRepository.deleteHabit("user@example.com", "Exercise");

        List<Habit> habits = habitRepository.findUserHabits("user@example.com");
        assertTrue(habits.isEmpty());
    }

    @Test
    @DisplayName("Should throw an exception when deleting a non-existent habit")
    void testDeleteHabitNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            habitRepository.deleteHabit("user@example.com", "NonExistentHabit");
        });
        assertEquals("No habits found for user: user@example.com", exception.getMessage());
    }

    @Test
    @DisplayName("Should delete all habits for a user successfully")
    void testDeleteAllUserHabits() {
        Habit habit = new Habit("Exercise", "Daily workout", "Daily");
        habitRepository.addHabit("user@example.com", habit);
        habitRepository.deleteAllUserHabits("user@example.com");

        List<Habit> habits = habitRepository.findUserHabits("user@example.com");
        assertTrue(habits.isEmpty());
    }

    @Test
    @DisplayName("Should throw an exception when deleting all habits for a user with no habits")
    void testDeleteAllUserHabitsNotFound() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            habitRepository.deleteAllUserHabits("user@example.com");
        });
        assertEquals("No habits found for user: user@example.com", exception.getMessage());
    }
}





