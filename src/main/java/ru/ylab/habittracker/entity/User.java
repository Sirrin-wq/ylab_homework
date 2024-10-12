package ru.ylab.habittracker.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private String name;
    private String email;
    private String password;
    private List<Habit> habitList;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.habitList = new ArrayList<>();
    }
}
