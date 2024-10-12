package ru.ylab.habittracker.service;

import ru.ylab.habittracker.entity.Habit;

import java.util.List;

public interface HabitService {

    List<Habit> getUserHabits(String email);

    Habit getHabitByUserEmail(String userEmail, String habitName);

    void addHabit(String userEmail, Habit habit);

    void updateHabit(String userEmail, Habit habit);

    void deleteHabit(String userEmail, String habitName);

    void deleteAllUserHabits(String userEmail);

    void markHabitCompleted(String userEmail, String habitName);

    List<Long> getHabitStatistics(String userEmail, String habitName, long startTime, long endTime);

}
