package ru.ylab.habittracker.repository;

import ru.ylab.habittracker.entity.User;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    User findByEmail(String email);

    void save(User user);

    void deleteByEmail(String email);
}
