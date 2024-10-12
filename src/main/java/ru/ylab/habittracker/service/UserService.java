package ru.ylab.habittracker.service;

import ru.ylab.habittracker.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getByEmail(String email);

    void addUser(String email, String password, String name);

    void updateUser(String email, String newName, String newPassword);

    void deleteUser(String email);

    void resetPassword(String email, String newPassword);
}
