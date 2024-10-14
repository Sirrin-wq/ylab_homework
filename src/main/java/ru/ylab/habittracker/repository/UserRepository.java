package ru.ylab.habittracker.repository;

import ru.ylab.habittracker.entity.User;
import ru.ylab.habittracker.exceptions.HabitNotFoundException;
import ru.ylab.habittracker.exceptions.UserAlreadyExistsException;
import ru.ylab.habittracker.exceptions.UserNotFoundException;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User findByEmail(String email) throws UserNotFoundException;

    void save(User user) throws UserAlreadyExistsException;

    void deleteByEmail(String email) throws UserNotFoundException, HabitNotFoundException;
}
