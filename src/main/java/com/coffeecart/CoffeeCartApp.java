package com.coffeecart;

import java.io.IOException;
import java.util.Scanner;

//Provides a user-friendly menu system to execute different test suites and generate reports.

public class CoffeeCartApp {

    //Users can choose to run specific test suites, select browsers, or generate reports.
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {

            System.out.println("1. Run all tests in Chrome and Firefox browsers");
            System.out.println("2. Run tests only in Chrome browser");
            System.out.println("3. Run tests only in Firefox browser");
            System.out.println("4. Run only menu tests");
            System.out.println("5. Run only cart tests");
            System.out.println("6. Run only checkout tests");
            System.out.println("7. Run end to end tests");
            System.out.println("8. Run all the tests and see Allure report");
            System.out.println("9. Go to main menu");
            System.out.println("10. Exit");
            System.out.print("\nEnter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    runAllTestsInBothBrowsers();
                    break;
                case 2:
                    runTestInChrome();
                    break;
                case 3:
                    runTestInFirefox();
                    break;
                case 4:
                    runMenuTestsOnly();
                    break;
                case 5:
                    runCartTestsOnly();
                    break;
                case 6:
                    runCheckoutTestsOnly();
                    break;
                case 7:
                    runEndToEndTests();
                    break;
                case 8:
                    runTestsWithAllure();
                    break;
                case 9:
                    continue;
                case 10:
                    running = false;
                    System.out.println("Thank you for trying out the CoffeeCart application");
                    break;
                default:
                    System.out.println("You've selected invalid choice. Kindly try again.");
            }
        }

        scanner.close();
    }


    private static void runAllTestsInBothBrowsers() {
        System.out.println("\nRunning all tests in Chrome and Firefox browsers");
        executeCommand("mvn clean test");
    }

    private static void runTestInChrome() {
        System.out.println("\nRunning tests in Chrome browser only");
        executeCommand("mvn test -P chrome-only");
    }

    private static void runTestInFirefox() {
        System.out.println("\nRunning tests in Firefox browser only");
        executeCommand("mvn test -P firefox-only");
    }


    private static void runMenuTestsOnly() {
        System.out.println("\nRunning menu tests");
        executeCommand("mvn test -Dtest=MenuTests");
    }

    private static void runCartTestsOnly() {
        System.out.println("\nRunning cart tests");
        executeCommand("mvn test -Dtest=CartTests");
    }

    private static void runCheckoutTestsOnly() {
        System.out.println("\nRunning checkout tests");
        executeCommand("mvn test -Dtest=CheckoutTests");
    }


    private static void runEndToEndTests() {
        System.out.println("\nRunning end to end tests");
        executeCommand("mvn test -Dtest=EndToEndTests");
    }


    private static void runTestsWithAllure() {
        System.out.println("\nRunning all tests and generating Allure report");
        executeCommand("mvn clean test allure:serve");
    }


    private static void executeCommand(String command) {
        try {
            System.out.println("Executing: " + command);

            ProcessBuilder pb = new ProcessBuilder();
            pb.inheritIO();

            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                pb.command("cmd", "/c", command);
            } else {
                pb.command("bash", "-c", command);
            }

            Process process = pb.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Command completed successfully");
            } else {
                System.out.println("Command completed with exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("Error executing command: " + e.getMessage());
        }
    }
}