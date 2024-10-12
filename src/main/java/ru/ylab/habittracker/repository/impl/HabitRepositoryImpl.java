package ru.ylab.habittracker.repository.impl;

import ru.ylab.habittracker.entity.Habit;
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
    public Habit findHabitByUserEmail(String userEmail, String habitName) {
        List<Habit> habits = findUserHabits(userEmail);
        return habits.stream()
                .filter(habit -> habit.getName().equals(habitName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Habit not found: " + habitName));
    }

    @Override
    public void addHabit(String email, Habit habit) {
        userHabits.putIfAbsent(email, new ArrayList<>());
        List<Habit> habits = userHabits.get(email);

        for (Habit existingHabit : habits) {
            if (existingHabit.getName().equals(habit.getName())) {
                throw new IllegalArgumentException("Habit already exists for this user: " + habit.getName());
            }
        }

        habits.add(habit);
    }

    @Override
    public void updateHabit(String email, Habit habit) {
        List<Habit> habits = userHabits.get(email);
        if (habits == null) {
            throw new IllegalArgumentException("No habits found for user: " + email);
        }

        for (Habit existingHabit : habits) {
            if (existingHabit.getName().equals(habit.getName())) {
                existingHabit.setDescription(habit.getDescription());
                existingHabit.setFrequency(habit.getFrequency());
                existingHabit.setCompleted(habit.isCompleted());
                return;
            }
        }

        throw new IllegalArgumentException("Habit not found for user: " + habit.getName());
    }

    @Override
    public void deleteHabit(String email, String habitName) {
        List<Habit> habits = userHabits.get(email);
        if (habits == null) {
            throw new IllegalArgumentException("No habits found for user: " + email);
        }

        habits.removeIf(habit -> habit.getName().equals(habitName));
    }

    @Override
    public void deleteAllUserHabits(String email) {
        List<Habit> habits = userHabits.get(email);
        if (habits == null) {
            throw new IllegalArgumentException("No habits found for user: " + email);
        }

        userHabits.remove(email);
    }
}
