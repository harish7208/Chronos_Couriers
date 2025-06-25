package org.example.util;

import org.example.model.PackagePriority;

import java.util.Scanner;

public class InputValidator {

    public static PackagePriority readValidPriority(Scanner scanner) {
        PackagePriority priority = null;
        while (priority == null) {
            System.out.print("Priority (EXPRESS/STANDARD): ");
            String input = scanner.nextLine().trim().toUpperCase();
            try {
                priority = PackagePriority.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid priority. Please enter EXPRESS or STANDARD.");
            }
        }
        return priority;
    }

    public static long readValidDeadline(Scanner scanner) {
        long deadline = 0;
        while (true) {
            System.out.print("Deadline (timestamp in milliseconds): ");
            try {
                deadline = Long.parseLong(scanner.nextLine());
                if (deadline <= System.currentTimeMillis()) {
                    System.out.println("Deadline must be in the future.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return deadline;
    }

    public static boolean readBoolean(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (true/false): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("false")) {
                return Boolean.parseBoolean(input);
            } else {
                System.out.println("Invalid input. Please enter true or false.");
            }
        }
    }
}
