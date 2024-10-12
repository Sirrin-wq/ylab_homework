package ru.ylab.habittracker.ui;

import lombok.RequiredArgsConstructor;
import ru.ylab.habittracker.entity.Habit;
import ru.ylab.habittracker.security.UserSecurityManager;
import ru.ylab.habittracker.service.HabitService;
import ru.ylab.habittracker.service.UserService;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class HabitTrackerDisplay {
    private final UserService userService;
    private final HabitService habitService;
    private final UserSecurityManager securityManager;

    public void displayMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println();
            System.out.println("=== Habit Tracker ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    registerUser(scanner);
                    break;
                case 2:
                    loginUser(scanner);
                    if (UserSecurityManager.currentUser != null) {
                        userMenu(scanner);
                    }
                    break;
                case 3:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void registerUser(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            securityManager.registerUser(name, email, password);
            System.out.println("User registered successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void loginUser(Scanner scanner) {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            securityManager.loginUser(email, password);
            System.out.println("Login successful!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void userMenu(Scanner scanner) {
        while (true) {
            System.out.println();
            System.out.println("=== User Menu ===");
            System.out.println("1. Add Habit");
            System.out.println("2. View Habits");
            System.out.println("3. Update Habit");
            System.out.println("4. Delete Habit");
            System.out.println("5. View Habit Statistics");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addHabit(scanner);
                    break;
                case 2:
                    viewHabits();
                    break;
                case 3:
                    updateHabit(scanner);
                    break;
                case 4:
                    deleteHabit(scanner);
                    break;
                case 5:
                    viewHabitStatistics(scanner);
                    break;
                case 6:
                    UserSecurityManager.currentUser = null;
                    System.out.println("Logged out successfully!");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void addHabit(Scanner scanner) {
        System.out.print("Enter habit name: ");
        String name = scanner.nextLine();
        System.out.print("Enter habit description: ");
        String description = scanner.nextLine();
        String frequency = "";
        int choice = 0;

        do {
            System.out.println("Enter habit frequency: ");
            System.out.println("1. Daily");
            System.out.println("3. Weekly");
            System.out.println("3. Monthly");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    frequency = "Daily";
                    break;
                case 2:
                    frequency = "Weekly";
                    break;
                case 3:
                    frequency = "Monthly";
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        } while (choice < 1 || choice > 3);

        Habit newHabit = new Habit(name, description, frequency);
        habitService.addHabit(UserSecurityManager.currentUser.getEmail(), newHabit);
        System.out.println("Habit added successfully!");
    }

    private void viewHabits() {
        List<Habit> habits = habitService.getUserHabits(UserSecurityManager.currentUser.getEmail());
        System.out.println("Your Habits:");
        for (int i = 0; i < habits.size(); i++) {
            Habit habit = habits.get(i);
            System.out.println((i + 1) + ". " + habit.getName() + " - " + (habit.isCompleted() ? "Completed" : "Not Completed"));
            System.out.println("description: " + habit.getDescription());
        }
    }

    private void updateHabit(Scanner scanner) {
        System.out.print("Enter the name of the habit to update: ");
        String habitName = scanner.nextLine();
        Habit existingHabit = habitService.getHabitByUserEmail(UserSecurityManager.currentUser.getEmail(), habitName);
        if (existingHabit == null) {
            System.out.println("Habit not found.");
            return;
        }

        System.out.print("Enter new description: ");
        String newDescription = scanner.nextLine();
        existingHabit.setDescription(newDescription);
        habitService.updateHabit(UserSecurityManager.currentUser.getEmail(), existingHabit);
        System.out.println("Habit updated successfully!");
    }

    private void deleteHabit(Scanner scanner) {
        System.out.print("Enter the name of the habit to delete: ");
        String habitName = scanner.nextLine();
        habitService.deleteHabit(UserSecurityManager.currentUser.getEmail(), habitName);
        System.out.println("Habit deleted successfully!");
    }

    private void viewHabitStatistics(Scanner scanner) {
        System.out.print("Enter habit name: ");
        String habitName = scanner.nextLine();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        long startTime = 0;
        long endTime = 0;

        while (true) {
            System.out.print("Enter start date (dd.MM.yyyy): ");
            String startDateInput = scanner.nextLine();
            try {
                LocalDate startDate = LocalDate.parse(startDateInput, formatter);
                startTime = startDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use dd:MM:yyyy.");
            }
        }

        while (true) {
            System.out.print("Enter end date (dd.MM.yyyy): ");
            String endDateInput = scanner.nextLine();
            try {
                LocalDate endDate = LocalDate.parse(endDateInput, formatter);
                endTime = endDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
                break;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use dd:MM:yyyy.");
            }
        }

        List<Long> statistics = habitService.getHabitStatistics(UserSecurityManager.currentUser.getEmail(), habitName, startTime, endTime);
        System.out.println("Habit Statistics: " + statistics);
    }
}

