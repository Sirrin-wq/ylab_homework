package ru.ylab.habittracker.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Habit {
    private String name;
    private String description;
    private String frequency;
    private boolean completed;
    private List<Long> timestamps;

    public Habit(String name, String description, String frequency) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.completed = false;
        this.timestamps = new ArrayList<>();
    }

    public void markCompleted() {
        long currentTime = System.currentTimeMillis();
        timestamps.add(currentTime);
    }

    public void addTimestamp(long timestamp) {
        timestamps.add(timestamp);
    }
}
