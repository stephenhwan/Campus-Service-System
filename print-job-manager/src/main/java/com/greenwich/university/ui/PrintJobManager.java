package com.greenwich.university.ui;

import java.time.LocalDateTime;
import java.util.Scanner;

public class PrintJobManager {
    private PrintJobQueue printQueue;
    private Scanner scanner;

    public PrintJobManager () {
        this.printQueue = new PrintJobQueue(100);
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu () {
        System.out.println("\n========================================");
        System.out.println("       PRINT JOB MANAGER SYSTEM       ");
        System.out.println("========================================");
        System.out.println("1. Submit a new print job");
        System.out.println("2. Serve the next print job in queue");
        System.out.println("3. Display all pending print jobs");
        System.out.println("4. Search for a job by file name");
        System.out.println("5. Display queue list");
        System.out.println("6. Exit");
        System.out.println("========================================");
        System.out.print("Please select an option (1-6): ");
    }

    public void submitPrintJob() {
        System.out.println("\n--- Submit New Print Job ---");

        System.out.print("File name: ");
        String fileName = scanner.nextLine().trim();
        if (fileName.isEmpty()) {
            System.out.println("Please enter a file name");
            return;
        }

        int pages = 0;
        while (pages <= 0) {
            System.out.print("Enter number of pages: ");
            try {
                pages = Integer.parseInt(scanner.nextLine().trim());
                if (pages <= 0) {
                    System.out.println("Error: Number of pages must be greater than 0!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number!");
            }
        }

        String priority = "";
        while (true) {
            System.out.print("Priority (HIGH/NORMAL/LOW): ");
            priority = scanner.nextLine().trim();
            if (priority.isEmpty()) {
                priority = "NORMAL";
            }
            if (priority.equalsIgnoreCase("HIGH") ||
                    priority.equalsIgnoreCase("NORMAL") ||
                    priority.equalsIgnoreCase("LOW")) {
                break;
            }
            System.out.println("Invalid priority. Please enter HIGH, NORMAL, or LOW.");
        }
        priority = priority.toUpperCase();

        PrintJob job = new PrintJob(fileName, pages, priority, LocalDateTime);
        if (printQueue.enqueue(job)) {
            System.out.println("✓ Print job submitted successfully!");
            System.out.println("Job Details:");
            System.out.println(job.getDetailedInfo());
        } else {
            System.out.println("✗ Failed to submit job. Queue is full!");
        }
    }

    /**
     * Serve the next print job in queue
     */
    public void serveNextJob() {
        System.out.println("\n--- Serve Next Print Job ---");

        if (printQueue.isEmpty()) {
            System.out.println("No jobs in queue to serve.");
            return;
        }

        PrintJob nextJob = printQueue.peek();
        System.out.println("Next job to be served:");
        System.out.println(nextJob.getDetailedInfo());

        System.out.print("Do you want to serve this job? (y/n): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("y") || confirm.equals("yes")) {
            PrintJob servedJob = printQueue.dequeue();
            System.out.println("✓ Print job served successfully!");
            System.out.println("Served: " + servedJob.toString());
            System.out.println("Remaining jobs in queue: " + printQueue.getSize());
        } else {
            System.out.println("Job serving cancelled.");
        }
    }

    public void displayAllJobs() {
        System.out.println("\n--- All Pending Print Jobs ---");

        if (printQueue.isEmpty()) {
            System.out.println("No pending print jobs in queue.");
            return;
        }

        PrintJob[] allJobs = printQueue.getAllJobs();
        System.out.println("Total jobs in queue: " + allJobs.length);
        System.out.println("----------------------------------------");

        for (int i = 0; i < allJobs.length; i++) {
            System.out.println((i + 1) + ". " + allJobs[i].toString());
        }

        System.out.println("----------------------------------------");
    }

    /**
     * Search for jobs by filename (substring, case-insensitive)
     */
    public void searchByFileName() {
        System.out.println("\n--- Search Jobs by File Name ---");

        if (printQueue.isEmpty()) {
            System.out.println("No jobs in queue to search.");
            return;
        }

        System.out.print("Enter file name to search: ");
        String filename = scanner.nextLine().trim();
        if (filename.isEmpty()) {
            System.out.println("Error: File name cannot be empty!");
            return;
        }

        String needle = filename.toLowerCase();
        PrintJob[] allJobs = printQueue.getAllJobs();
        boolean found = false;

        for (int i = 0; i < allJobs.length; i++) {
            if (allJobs[i].getFileName().toLowerCase().contains(needle)) {
                if (!found) {
                    System.out.println("Matches:");
                    found = true;
                }
                System.out.println(" - " + allJobs[i].getDetailedInfo());
            }
        }

        if (!found) {
            System.out.println("No job matched: " + filename);
        }
    }

    /**
     * Display queue statistics
     */
    public void displayList() {
        System.out.println("\n--- Queue List ---");
        System.out.println("Current jobs in queue: " + printQueue.getSize());
        System.out.println("Queue capacity: " + printQueue.getCapacity());
        System.out.println("Available slots: " + (printQueue.getCapacity() - printQueue.getSize()));

        if (!printQueue.isEmpty()) {
            PrintJob nextJob = printQueue.peek();
            System.out.println("Next job to be served: " + nextJob.toString());
        }
    }

    /**
     * Run the main program loop
     */
    public void run() {
        System.out.println("Welcome to Print Job Manager System!");

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
                        displayList();
                        break;
                    case 6:
                        System.out.println("Thank you for using Print Job Manager System!");
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option! Please select 1-6.");
                }

                // Pause before showing menu again
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();

            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number!");
            }
        }
    }
}
