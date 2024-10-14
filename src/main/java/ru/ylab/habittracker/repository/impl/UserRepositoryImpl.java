package ru.ylab.habittracker.repository.impl;

import lombok.RequiredArgsConstructor;
import ru.ylab.habittracker.entity.User;
import ru.ylab.habittracker.exceptions.HabitNotFoundException;
import ru.ylab.habittracker.exceptions.UserAlreadyExistsException;
import ru.ylab.habittracker.exceptions.UserNotFoundException;
import ru.ylab.habittracker.repository.HabitRepository;
import ru.ylab.habittracker.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final HabitRepository habitRepository;
    private final Map<String, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void save(User user) throws UserAlreadyExistsException {
        String email = user.getEmail();
        if (users.containsKey(email)) {
            throw new UserAlreadyExistsException("User with this email already exists: " + email);
        }
        users.put(email, user);
    }

    @Override
    public User findByEmail(String email) throws UserNotFoundException {
        User user = users.get(email);
        if (user == null) {
            throw new UserNotFoundException("User with this email does not exist: " + email);
        }
        return user;
    }


    @Override
    public void deleteByEmail(String email) throws UserNotFoundException, HabitNotFoundException {
        if (users.containsKey(email)) {
            habitRepository.deleteAllUserHabits(email);
            users.remove(email);
        } else {
            throw new UserNotFoundException("User with this email does not exist: " + email);
        }
    }
}

