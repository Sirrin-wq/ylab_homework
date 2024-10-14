package ru.ylab.habittracker.exceptions;

public class HabitNotFoundException extends Exception {
    public HabitNotFoundException(String message) {
        super(message);
    }
}
