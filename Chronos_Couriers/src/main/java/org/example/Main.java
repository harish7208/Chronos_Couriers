package org.example;

import org.example.service.DispatchCenter;
import org.example.util.MenuHandler;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        DispatchCenter dispatchCenter = new DispatchCenter();
        Scanner scanner = new Scanner(System.in);
        MenuHandler menu = new MenuHandler(dispatchCenter, scanner);

        boolean run = true;
        while (run) {
            menu.displayMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                run = menu.handleChoice(choice);
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}

