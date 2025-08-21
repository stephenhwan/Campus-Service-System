
package com.greenwich.university;

import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("🎓 Welcome to Greenwich University Campus System!");

        while (true) {
            displayMainMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1:
                        runPrintJobManager();
                        break;
                    case 2:
                        runCourseRegistration();
                        break;
                    case 3:
                        runDigitalLibrary();
                        break;
                    case 4:
                        runLostAndFound();
                        break;
                    case 5:
                        runRoomBooking();
                        break;
                    case 6:
                        System.out.println("👋 Thank you for using Greenwich University Campus System!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("❌ Invalid option! Please select 1-7.");
                }

                System.out.println("\nPress Enter to return to main menu...");
                scanner.nextLine();

            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            } catch (Exception e) {
                System.err.println("❌ Error occurred: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n" + "=".repeat(55));
        System.out.println("         GREENWICH UNIVERSITY CAMPUS SYSTEM");
        System.out.println("=".repeat(55));
        System.out.println("1. 🖨️  Print Job Manager");
        System.out.println("2. 📚 Course Registration");
        System.out.println("3. 📖 Digital Library System");
        System.out.println("4. 📋 Lost and Found Tracker");
        System.out.println("5. 🏢 Room Booking System");
        System.out.println("6. 🚪 Exit");
        System.out.println("=".repeat(55));
        System.out.print("Select option (1-6): ");
    }

    private static void runPrintJobManager() {
        try {
            System.out.println("\n🖨️ Starting Print Job Manager...");
            PrintJobManagerAPI printAPI = new PrintJobManagerAPI();
            printAPI.start();
        } catch (Exception e) {
            System.err.println("❌ Error running Print Job Manager: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runCourseRegistration() {
        try {
            System.out.println("\n📚 Starting Course Registration...");
            CourseRegistrationAPI courseAPI = new CourseRegistrationAPI();
            courseAPI.start();
        } catch (Exception e) {
            System.err.println("❌ Error running Course Registration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runDigitalLibrary() {
        try {
            System.out.println("\n📖 Starting Digital Library System...");
            DigitalLibraryAPI libraryAPI = new DigitalLibraryAPI();
            libraryAPI.start();
        } catch (Exception e) {
            System.err.println("❌ Error running Digital Library System: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runLostAndFound() {
        try {
            System.out.println("\n📋 Starting Lost and Found Tracker...");
            LostAndFoundAPI lostFoundAPI = new LostAndFoundAPI();
            lostFoundAPI.start();
        } catch (Exception e) {
            System.err.println("❌ Error running Lost and Found Tracker: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runRoomBooking() {
        try {
            System.out.println("\n🏢 Starting Room Booking System...");
            RoomBookingAPI roomAPI = new RoomBookingAPI();
            roomAPI.start();
        } catch (Exception e) {
            System.err.println("❌ Error running Room Booking System: " + e.getMessage());
            e.printStackTrace();
        }
    }
}