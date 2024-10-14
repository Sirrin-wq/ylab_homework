package ru.ylab.habittracker;

import ru.ylab.habittracker.ui.HabitTrackerDisplay;
import ru.ylab.habittracker.repository.HabitRepository;
import ru.ylab.habittracker.repository.UserRepository;
import ru.ylab.habittracker.repository.impl.HabitRepositoryImpl;
import ru.ylab.habittracker.repository.impl.UserRepositoryImpl;
import ru.ylab.habittracker.security.UserSecurityManager;
import ru.ylab.habittracker.service.HabitService;
import ru.ylab.habittracker.service.UserService;
import ru.ylab.habittracker.service.impl.HabitServiceImpl;
import ru.ylab.habittracker.service.impl.UserServiceImpl;

public class HabitTrackerApp {
    public static void main(String[] args) {
        HabitRepository habitRepository = new HabitRepositoryImpl();
        UserRepository userRepository = new UserRepositoryImpl(habitRepository);
        HabitService habitService = new HabitServiceImpl(habitRepository);
        UserService userService = new UserServiceImpl(userRepository);
        UserSecurityManager securityManager = new UserSecurityManager(userService);

        HabitTrackerDisplay app = new HabitTrackerDisplay(userService, habitService, securityManager);
        app.displayMenu();
    }
}