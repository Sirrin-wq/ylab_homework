package ru.ylab.habittracker.repository;

import ru.ylab.habittracker.entity.Habit;

import java.util.List;

public interface HabitRepository {
    List<Habit> findUserHabits(String email);

    Habit findHabitByUserEmail(String userEmail, String habitName);

    void addHabit(String email, Habit habit);

    void updateHabit(String email, Habit habit);

    void deleteHabit(String email, String habitName);

    void deleteAllUserHabits(String email);
}
