package ru.ylab.habittracker.service.impl;

import lombok.RequiredArgsConstructor;
import ru.ylab.habittracker.entity.Habit;
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
        return habitRepository.findHabitByUserEmail(userEmail, habitName);
    }

    @Override
    public void addHabit(String userEmail, Habit habit) {
        habitRepository.addHabit(userEmail, habit);
    }

    @Override
    public void updateHabit(String userEmail, Habit habit) {
        habitRepository.updateHabit(userEmail, habit);
    }

    @Override
    public void deleteHabit(String userEmail, String habitName) {
        habitRepository.deleteHabit(userEmail, habitName);
    }

    @Override
    public void deleteAllUserHabits(String userEmail) {
        habitRepository.deleteAllUserHabits(userEmail);
    }

    @Override
    public void markHabitCompleted(String userEmail, String habitName) {
        Habit existingHabit = habitRepository.findHabitByUserEmail(userEmail, habitName);
        existingHabit.markCompleted();
    }

    @Override
    public List<Long> getHabitStatistics(String userEmail, String habitName, long startTime, long endTime) {
        Habit existingHabit = habitRepository.findHabitByUserEmail(userEmail, habitName);
        return existingHabit.getTimestamps().stream()
                .filter(timestamp -> timestamp >= startTime && timestamp <= endTime)
                .collect(Collectors.toList());
    }
}
