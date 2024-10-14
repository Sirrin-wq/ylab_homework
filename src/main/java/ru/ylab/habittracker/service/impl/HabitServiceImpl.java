package ru.ylab.habittracker.service.impl;

import lombok.RequiredArgsConstructor;
import ru.ylab.habittracker.entity.Habit;
import ru.ylab.habittracker.exceptions.HabitAlreadyExistsException;
import ru.ylab.habittracker.exceptions.HabitNotFoundException;
import ru.ylab.habittracker.repository.HabitRepository;
import ru.ylab.habittracker.service.HabitService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {
    private final HabitRepository habitRepository;


    @Override
    public List<Habit> getUserHabits(String email) {
        return habitRepository.findUserHabits(email);
    }

    @Override
    public Habit getHabitByUserEmail(String userEmail, String habitName) {
        Habit habit = null;
        try {
            habit = habitRepository.findHabitByUserEmail(userEmail, habitName);
        } catch (HabitNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return habit;
    }

    @Override
    public void addHabit(String userEmail, Habit habit) {
        try {
            habitRepository.addHabit(userEmail, habit);
        } catch (HabitAlreadyExistsException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void updateHabit(String userEmail, Habit habit) {
        try {
            habitRepository.updateHabit(userEmail, habit);
        } catch (HabitNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void deleteHabit(String userEmail, String habitName) {
        try {
            habitRepository.deleteHabit(userEmail, habitName);
        } catch (HabitNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void deleteAllUserHabits(String userEmail) {
        try {
            habitRepository.deleteAllUserHabits(userEmail);
        } catch (HabitNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void markHabitCompleted(String userEmail, String habitName) {
        try {
            Habit existingHabit = habitRepository.findHabitByUserEmail(userEmail, habitName);
            existingHabit.markCompleted();
        } catch (HabitNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public List<Long> getHabitStatistics(String userEmail, String habitName, long startTime, long endTime) {
        Habit existingHabit = null;
        try {
            existingHabit = habitRepository.findHabitByUserEmail(userEmail, habitName);
        } catch (HabitNotFoundException exception){
            System.out.println(exception.getMessage());
        }
        return existingHabit.getTimestamps().stream()
                .filter(timestamp -> timestamp >= startTime && timestamp <= endTime)
                .collect(Collectors.toList());
    }
}
