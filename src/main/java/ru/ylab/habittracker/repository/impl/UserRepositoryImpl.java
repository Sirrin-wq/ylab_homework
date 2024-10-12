package ru.ylab.habittracker.repository.impl;

import ru.ylab.habittracker.entity.User;
import ru.ylab.habittracker.repository.HabitRepository;
import ru.ylab.habittracker.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserRepositoryImpl implements UserRepository {
    private final HabitRepository habitRepository;
    private final Map<String, User> users = new HashMap<>();

    public UserRepositoryImpl(HabitRepository habitRepository) {
        this.habitRepository = habitRepository;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void save(User user) {
        String email = user.getEmail();
        if (users.containsKey(email)) {
            throw new IllegalArgumentException("User with this email already exists: " + email);
        }
        users.put(email, user);
    }

    @Override
    public User findByEmail(String email) {
        User user = users.get(email);
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }
        return user;
    }


    @Override
    public void deleteByEmail(String email) {
        if (users.containsKey(email)) {
            habitRepository.deleteAllUserHabits(email);
            users.remove(email);
        } else {
            throw new IllegalArgumentException("User with this email does not exist: " + email);
        }
    }
}

