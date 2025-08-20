package com.greenwich.university.ui;

import com.greenwich.university.domain.PrintJob;
import com.greenwich.university.repository.PrintJobQueue;
import java.util.Scanner;

public class PrintJobManager {
    private PrintJobQueue printQueue;
    private Scanner scanner;

    public PrintJobManager() {
        this.printQueue = new PrintJobQueue(100);
        this.scanner = new Scanner(System.in);
    }

    public void displayMenu() {
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
            System.out.println("❌ Please enter a file name");
            return;
        }

        int pages = getValidPages();
        String priority = getValidPriority();

        // ĐÃ SỬA: Constructor đơn giản với 3 tham số
        PrintJob job = new PrintJob(fileName, pages, priority);

        if (printQueue.enqueue(job)) {
            System.out.println("✅ Print job submitted successfully!");
            System.out.println("Job Details:");
            System.out.println(job.getDetailedInfo());
        } else {
            System.out.println("❌ Failed to submit job. Queue is full!");
        }
    }

    // Đơn giản hóa: Tách logic validation
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

            if (PrintJob.isValidPriority(priority)) {
                return priority.toUpperCase();
            }

            System.out.println("❌ Invalid priority. Please enter HIGH, NORMAL, or LOW.");
        }
    }

    public void serveNextJob() {
        System.out.println("\n--- Serve Next Print Job ---");

        if (printQueue.isEmpty()) {
            System.out.println("📭 No jobs in queue to serve.");
            return;
        }

        PrintJob nextJob = printQueue.peek();
        System.out.println("Next job to be served:");
        System.out.println(nextJob.getDetailedInfo());

        if (confirmAction("Do you want to serve this job? (y/n): ")) {
            PrintJob servedJob = printQueue.dequeue();
            System.out.println("✅ Print job served successfully!");
            System.out.println("Served: " + servedJob.toString());
            System.out.println("📊 Remaining jobs in queue: " + printQueue.getSize());
        } else {
            System.out.println("❌ Job serving cancelled.");
        }
    }

    // Đơn giản hóa: Tách logic confirmation
    private boolean confirmAction(String message) {
        System.out.print(message);
        String confirm = scanner.nextLine().trim().toLowerCase();
        return confirm.equals("y") || confirm.equals("yes");
    }

    public void displayAllJobs() {
        System.out.println("\n--- All Pending Print Jobs ---");

        if (printQueue.isEmpty()) {
            System.out.println("📭 No pending print jobs in queue.");
            return;
        }

        PrintJob[] allJobs = printQueue.getAllJobs();
        System.out.println("📊 Total jobs in queue: " + allJobs.length);
        System.out.println("----------------------------------------");

        for (int i = 0; i < allJobs.length; i++) {
            System.out.println((i + 1) + ". " + allJobs[i].toString());
        }

        System.out.println("----------------------------------------");
    }

    public void searchByFileName() {
        System.out.println("\n--- Search Jobs by File Name ---");

        if (printQueue.isEmpty()) {
            System.out.println("📭 No jobs in queue to search.");
            return;
        }

        System.out.print("Enter file name to search: ");
        String filename = scanner.nextLine().trim();
        if (filename.isEmpty()) {
            System.out.println("❌ File name cannot be empty!");
            return;
        }

        // Sử dụng method có sẵn từ PrintJobQueue
        PrintJob[] matches = printQueue.searchByFileName(filename);

        if (matches.length > 0) {
            System.out.println("🔍 Found " + matches.length + " match(es):");
            for (int i = 0; i < matches.length; i++) {
                System.out.println((i + 1) + ". " + matches[i].toString());
            }
        } else {
            System.out.println("❌ No jobs found matching: " + filename);
        }
    }

    public void displayList() {
        System.out.println("\n--- Queue List ---");
        System.out.println("📊 Current jobs in queue: " + printQueue.getSize());
        System.out.println("📦 Queue capacity: " + printQueue.getCapacity());
        System.out.println("🆓 Available slots: " + (printQueue.getCapacity() - printQueue.getSize()));

        // Hiển thị thống kê theo priority
        var priorityCount = printQueue.getPriorityCount();
        System.out.println("🔴 HIGH priority jobs: " + priorityCount.high);
        System.out.println("🟡 NORMAL priority jobs: " + priorityCount.normal);
        System.out.println("🟢 LOW priority jobs: " + priorityCount.low);

        if (!printQueue.isEmpty()) {
            PrintJob nextJob = printQueue.peek();
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
                        displayList(); // Đổi tên method
                        break;
                    case 6:
                        System.out.println("👋 Thank you for using Print Job Manager System!");
                        System.out.println("Goodbye!");
                        scanner.close(); // Đóng scanner
                        return;
                    default:
                        System.out.println("❌ Invalid option! Please select 1-6.");
                }

                // Pause before showing menu again
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();

            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
        }
    }
}