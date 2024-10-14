package ru.ylab.habittracker.repository;

import ru.ylab.habittracker.entity.Habit;
import ru.ylab.habittracker.exceptions.HabitAlreadyExistsException;
import ru.ylab.habittracker.exceptions.HabitNotFoundException;

import java.util.List;

public interface HabitRepository {
    List<Habit> findUserHabits(String email);

    Habit findHabitByUserEmail(String userEmail, String habitName) throws HabitNotFoundException;;

    void addHabit(String email, Habit habit) throws HabitAlreadyExistsException;

    void updateHabit(String email, Habit habit) throws HabitNotFoundException;

    void deleteHabit(String email, String habitName) throws HabitNotFoundException;

    void deleteAllUserHabits(String email) throws HabitNotFoundException;
}
