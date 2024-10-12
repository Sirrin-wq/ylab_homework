package ru.ylab.habittracker.service.impl;

import lombok.RequiredArgsConstructor;
import ru.ylab.habittracker.entity.User;
import ru.ylab.habittracker.repository.UserRepository;
import ru.ylab.habittracker.service.UserService;

import java.util.List;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void addUser(String email, String password, String name) {
        User newUser = new User(email, password, name);
        userRepository.save(newUser);
    }

    @Override
    public void updateUser(String email, String newName, String newPassword) {
        User user = userRepository.findByEmail(email);
        user.setName(newName);
        user.setPassword(newPassword);
    }

    @Override
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        user.setPassword(newPassword);
    }
}
