package com.greenwich.university.ui;

import com.greenwich.university.appService.PrintJobService;
import com.greenwich.university.domain.PrintJob;
import java.util.Scanner;

/**
 * UI Layer - Handles user interaction and presentation
 * Depends only on Service layer
 */
public class PrintJobManager {
    private PrintJobService printJobService; // Only dependency on Service layer
    private Scanner scanner;

    public PrintJobManager() {
        this.printJobService = new PrintJobService(); // Dependency injection point
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
        System.out.println("\n=============================================");
        System.out.println("       PRINT JOB MANAGER SYSTEM       ");
        System.out.println("=============================================");
        System.out.println("---------------------------------------------");
        System.out.println("             QUEUE STATISTIC            ");
        String stats = printJobService.getQueueStats();
        System.out.println("📊 " + stats);
        if (!printJobService.isEmpty()) {
            PrintJob nextJob = printJobService.getNextJob();
            System.out.println("⭐️ Next job to be served: " + nextJob.toString());
        } else {
            System.out.println("📭 No jobs in queue");
        }
        System.out.println("---------------------------------------------");
        System.out.println("1. Submit a new print job");
        System.out.println("2. Serve the next print job in queue");
        System.out.println("3. Display all pending print jobs");
        System.out.println("4. Search for a job by file name");
        System.out.println("5. Display queue statistic");
        System.out.println("6. Exit");
        System.out.println("=============================================");
        System.out.print("Please select an option (1-6): ");
    }

    public void submitPrintJob() {
        System.out.println("\n--- Submit New Print Job ---");

        System.out.print("File name: ");
        String fileName = scanner.nextLine().trim();

        int pages = getValidPages();
        String priority = getValidPriority();

        // Delegate business logic to service layer
        String result = printJobService.submitJob(fileName, pages, priority);

        if (result.startsWith("Job submitted:")) {
            System.out.println("✅ " + result);
        } else {
            System.out.println("❌ " + result);
        }
    }

    private int getValidPages() {
        while (true) {
            System.out.print("Enter number of pages: ");
            try {
                int pages = Integer.parseInt(scanner.nextLine().trim());
                if (pages > 0) {
                    return pages;
                }
                System.out.println("❌ Number of pages must be greater than 0!");
            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
        }
    }

    private String getValidPriority() {
        while (true) {
            System.out.print("Priority (HIGH/NORMAL/LOW) [Default: NORMAL]: ");
            String priority = scanner.nextLine().trim();

            if (priority.isEmpty()) {
                return "NORMAL";
            }

            // Use domain validation through service
            if (PrintJob.isValidPriority(priority)) {
                return priority.toUpperCase();
            }

            System.out.println("❌ Invalid priority. Please enter HIGH, NORMAL, or LOW.");
        }
    }

    public void serveNextJob() {
        System.out.println("\n--- Serve Next Print Job ---");

        // Check through service layer
        if (printJobService.isEmpty()) {
            System.out.println("📭 No jobs in queue to serve.");
            return;
        }

        PrintJob nextJob = printJobService.getNextJob();
        System.out.println("Next job to be served:");
        System.out.println(nextJob.getDetailedInfo());

        if (confirmAction("Do you want to serve this job? (y/n): ")) {
            String result = printJobService.serveNextJob();
            System.out.println("✅ " + result);
        } else {
            System.out.println("❌ Job serving cancelled.");
        }
    }

    private boolean confirmAction(String message) {
        System.out.print(message);
        String confirm = scanner.nextLine().trim().toLowerCase();
        return confirm.equals("y") || confirm.equals("yes");
    }

    public void displayAllJobs() {
        System.out.println("\n--- All Pending Print Jobs ---");

        if (printJobService.isEmpty()) {
            System.out.println("📭 No pending print jobs in queue.");
            return;
        }

        PrintJob[] allJobs = printJobService.getAllJobs();
        System.out.println("📊 Total jobs in queue: " + allJobs.length);
        System.out.println("----------------------------------------");

        for (int i = 0; i < allJobs.length; i++) {
            System.out.println((i + 1) + ". " + allJobs[i].toString());
        }

        System.out.println("----------------------------------------");
    }

    public void searchByFileName() {
        System.out.println("\n--- Search Jobs by File Name ---");

        if (printJobService.isEmpty()) {
            System.out.println("📭 No jobs in queue to search.");
            return;
        }

        System.out.print("Enter file name to search: ");
        String filename = scanner.nextLine().trim();

        // Delegate to service layer
        PrintJob[] matches = printJobService.searchByFileName(filename);

        if (matches.length > 0) {
            System.out.println("🔍 Found " + matches.length + " match(es):");
            for (int i = 0; i < matches.length; i++) {
                System.out.println((i + 1) + ". " + matches[i].toString());
            }
        } else {
            if (filename.isEmpty()) {
                System.out.println("❌ File name cannot be empty!");
            } else {
                System.out.println("❌ No jobs found matching: " + filename);
            }
        }
    }

    public void displayQueueStatistic() {
        System.out.println("\n--- Queue List ---");

        // Get statistics through service layer
        String stats = printJobService.getQueueStats();
        System.out.println("📊 " + stats);

        if (!printJobService.isEmpty()) {
            PrintJob nextJob = printJobService.getNextJob();
            System.out.println("⏭️ Next job to be served: " + nextJob.toString());
        }
    }

    public void run() {
        System.out.println("🎉 Welcome to Print Job Manager System!");

        while (true) {
            displayMenu();

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1:
                        submitPrintJob();
                        break;
                    case 2:
                        serveNextJob();
                        break;
                    case 3:
                        displayAllJobs();
                        break;
                    case 4:
                        searchByFileName();
                        break;
                    case 5:
                        displayQueueStatistic();
                        break;
                    case 6:
                        System.out.println("👋 Thank you for using Print Job Manager System!");
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("❌ Invalid option! Please select 1-6.");
                }

                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();

            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
        }
    }
}