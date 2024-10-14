package ru.ylab.habittracker.repository.impl;

import ru.ylab.habittracker.entity.Habit;
import ru.ylab.habittracker.exceptions.HabitAlreadyExistsException;
import ru.ylab.habittracker.exceptions.HabitNotFoundException;
import ru.ylab.habittracker.repository.HabitRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.Map;

public class HabitRepositoryImpl implements HabitRepository {
    private final Map<String, List<Habit>> userHabits = new HashMap<>();

    @Override
    public List<Habit> findUserHabits(String email) {
        return userHabits.getOrDefault(email, new ArrayList<>());
    }

    @Override
    public Habit findHabitByUserEmail(String userEmail, String habitName) throws HabitNotFoundException {
        List<Habit> habits = findUserHabits(userEmail);
        return habits.stream()
                .filter(habit -> habit.getName().equals(habitName))
                .findFirst()
                .orElseThrow(() -> new HabitNotFoundException("Habit not found: " + habitName));
    }

    @Override
    public void addHabit(String email, Habit habit) throws HabitAlreadyExistsException {
        userHabits.putIfAbsent(email, new ArrayList<>());
        List<Habit> habits = userHabits.get(email);

        for (Habit existingHabit : habits) {
            if (existingHabit.getName().equals(habit.getName())) {
                throw new HabitAlreadyExistsException("Habit already exists for this user: " + habit.getName());
            }
        }

        habits.add(habit);
    }

    @Override
    public void updateHabit(String email, Habit habit) throws HabitNotFoundException {
        List<Habit> habits = userHabits.get(email);
        if (habits == null) {
            throw new HabitNotFoundException("No habits found for user: " + email);
        }

        for (Habit existingHabit : habits) {
            if (existingHabit.getName().equals(habit.getName())) {
                existingHabit.setDescription(habit.getDescription());
                existingHabit.setFrequency(habit.getFrequency());
                existingHabit.setCompleted(habit.isCompleted());
                return;
            }
        }

        throw new HabitNotFoundException("Habit not found for user: " + habit.getName());
    }

    @Override
    public void deleteHabit(String email, String habitName) throws HabitNotFoundException {
        List<Habit> habits = userHabits.get(email);
        if (habits == null) {
            throw new HabitNotFoundException("No habits found for user: " + email);
        }

        habits.removeIf(habit -> habit.getName().equals(habitName));
    }

    @Override
    public void deleteAllUserHabits(String email) throws HabitNotFoundException {
        List<Habit> habits = userHabits.get(email);
        if (habits == null) {
            throw new HabitNotFoundException("No habits found for user: " + email);
        }

        userHabits.remove(email);
    }
}
