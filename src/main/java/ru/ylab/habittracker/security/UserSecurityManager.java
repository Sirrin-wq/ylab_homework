package ru.ylab.habittracker.security;

import lombok.RequiredArgsConstructor;
import ru.ylab.habittracker.entity.User;
import ru.ylab.habittracker.service.UserService;

@RequiredArgsConstructor
public class UserSecurityManager {
    private final UserService userService;
    public static User currentUser = null;

    public void registerUser(String name, String email, String password) {
        userService.addUser(name, email, password);
    }

    public User loginUser(String email, String password) {
        User user = userService.getByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return user;
        } else {
            throw new IllegalArgumentException("Incorrect email or password");
        }
    }
}
