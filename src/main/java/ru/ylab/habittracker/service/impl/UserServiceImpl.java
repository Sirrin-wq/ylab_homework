package ru.ylab.habittracker.service.impl;

import lombok.RequiredArgsConstructor;
import ru.ylab.habittracker.entity.User;
import ru.ylab.habittracker.exceptions.UserAlreadyExistsException;
import ru.ylab.habittracker.exceptions.UserNotFoundException;
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
        User user = null;
        try {
            user = userRepository.findByEmail(email);
        } catch (UserNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        return user;
    }

    @Override
    public void addUser(String email, String password, String name) {
        User newUser = new User(email, password, name);
        try {
            userRepository.save(newUser);
        } catch (UserAlreadyExistsException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void updateUser(String email, String newName, String newPassword) {
        User user = null;
        try {
            user = userRepository.findByEmail(email);
            user.setName(newName);
            user.setPassword(newPassword);
        } catch (UserNotFoundException exception) {
            System.out.println(exception.getMessage());
        }

    }

    @Override
    public void deleteUser(String email) {
        try {
            userRepository.deleteByEmail(email);
        } catch (UserNotFoundException exception){
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        try {
            User user = userRepository.findByEmail(email);
            user.setPassword(newPassword);
        } catch (UserNotFoundException exception){
            System.out.println(exception.getMessage());
        }

    }
}
