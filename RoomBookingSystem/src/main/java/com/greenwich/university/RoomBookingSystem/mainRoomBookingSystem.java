package com.greenwich.university.RoomBookingSystem;

// Import classes to handle dates, times, formatting, and user input

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

// RoomBookingApp is the main console application (UI) for the Room Booking System
public class mainRoomBookingSystem {

    // Formatter for parsing and displaying times in "hour:minute" format (24h clock)
    private static final DateTimeFormatter TF = DateTimeFormatter.ofPattern("H:mm");
    // Scanner object for reading user input from console
    private static final Scanner sc = new Scanner(System.in);

    // Main method: entry point of the program
    public void run() {
        BookingManager mgr = new BookingManager(); // Controller object to manage bookings
        while (true) { // Infinite loop for the main menu
            // Display main menu options
            System.out.println("\n=== ROOM BOOKING SYSTEM ===");
            System.out.println("1) Add booking");
            System.out.println("2) Check availability");
            System.out.println("3) Cancel booking");
            System.out.println("4) Display all bookings (sorted by date/time)");
            System.out.println("0) Exit");
            System.out.print("Choose: ");

            // Read user choice (trim removes extra spaces)
            String opt = sc.nextLine().trim();
            try {
                // Handle menu options using switch
                switch (opt) {
                    case "1": onAdd(mgr); break;      // Add new booking
                    case "2": onCheck(mgr); break;    // Check availability
                    case "3": onCancel(mgr); break;   // Cancel booking
                    case "4": onDisplay(mgr); break;  // Show all bookings
                    case "0": // Exit program
                        System.out.println("Goodbye!");
                        return;
                    default:
                        // If input is invalid
                        System.out.println("Invalid choice.");
                }
            } catch (Exception ex) {
                // Catch and show any runtime error (e.g., invalid date format)
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    // Handle "Add booking" option
    private static void onAdd(BookingManager mgr) {
        String room = ask("Room (e.g., A101): ");                  // Get room
        LocalDate date = LocalDate.parse(ask("Date (YYYY-MM-DD): ")); // Get date
        LocalTime start = LocalTime.parse(ask("Start (H:MM, 24h): "), TF); // Get start time
        LocalTime end = LocalTime.parse(ask("End (H:MM, 24h): "), TF);     // Get end time
        String by = ask("Booked by (name): ");                    // Get booker's name

        // Try to add booking via BookingManager
        Booking b = mgr.add(room, date, start, end, by);
        // Print result (success or conflict)
        System.out.println(b == null ? "Slot NOT available." : ("Added: " + b));
    }

    // Handle "Check availability" option
    private static void onCheck(BookingManager mgr) {
        String room = ask("Room: ");                                  // Get room
        LocalDate date = LocalDate.parse(ask("Date (YYYY-MM-DD): ")); // Get date
        LocalTime start = LocalTime.parse(ask("Start (H:MM, 24h): "), TF); // Get start time
        LocalTime end = LocalTime.parse(ask("End (H:MM, 24h): "), TF);     // Get end time
        // Print result of availability check
        System.out.println(mgr.isAvailable(room, date, start, end)
                ? "Available ✅" : "Not available ❌");
    }

    // Handle "Cancel booking" option
    private static void onCancel(BookingManager mgr) {
        int id = Integer.parseInt(ask("Booking ID to cancel: ")); // Get ID to cancel
        // Try to cancel via BookingManager and print result
        System.out.println(mgr.cancel(id) ? "Cancelled." : "Booking not found.");
    }

    // Handle "Display all bookings" option
    private static void onDisplay(BookingManager mgr) {
        Booking[] arr = mgr.listSortedByDateTime(); // Get sorted list of bookings
        if (arr.length == 0) {
            System.out.println("(no bookings)"); // If no bookings exist
            return;
        }
        System.out.println("-- All bookings (chronological) --");
        // Print each booking
        for (Booking b : arr) System.out.println(b);
    }

    // Utility method: ask a question and read user input
    private static String ask(String msg) {
        System.out.print(msg);           // Display prompt
        return sc.nextLine().trim();     // Return trimmed user input
    }
}
