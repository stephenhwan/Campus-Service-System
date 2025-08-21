package com.greenwich.university.ui;
import com.greenwich.university.appService.PrintJobService;
import com.greenwich.university.domain.PrintJob;
import java.util.Scanner;
/**
 * UI Layer - Handles user interaction and presentation
 * Simplified analytics without bottleneck detection and recommendations
 */
public class PrintJobManager {
    private PrintJobService service;
    private Scanner scanner;
    public PrintJobManager() {
        this.service = new PrintJobService();
        this.scanner = new Scanner(System.in);
    }
    private void displayMenu() {
        System.out.println("\n" + "=".repeat(45));
        System.out.println("       PRINT JOB MANAGER SYSTEM");
        System.out.println("=".repeat(45));

        System.out.println("📊 " + service.getBasicStats());
        if (!service.isEmpty()) {
            System.out.println("⭐ Next: " + service.getNextJob());
        } else {
            System.out.println("📭 No jobs in queue");
        }

        System.out.println("-".repeat(45));
        System.out.println("1. Submit new job");
        System.out.println("2. Serve next job");
        System.out.println("3. Display all jobs");
        System.out.println("4. Search by filename");
        System.out.println("5. Display Queue Statistic");
        System.out.println("6. Monitoring Queue ");
        System.out.println("7. Exit");
        System.out.println("=".repeat(45));
        System.out.print("Select option (1-7): ");
    }


    private String createProgressBar(double pct, int width) {
        int filled = (int) (pct * width / 100);
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < width; i++) {
            bar.append(i < filled ? "█" : "░");
        }
        return bar.append("]").toString();
    }

    private void submitJob() {
        System.out.println("\n--- Submit New Job ---");
        System.out.print("File name: ");
        String fileName = scanner.nextLine().trim();

        int pages = getValidPages();
        String priority = getValidPriority();

        System.out.println(service.submitJob(fileName, pages, priority));
    }

    private int getValidPages() {
        while (true) {
            System.out.print("Pages: ");
            try {
                int pages = Integer.parseInt(scanner.nextLine().trim());
                if (pages > 0) return pages;
                System.out.println("❌ Pages must be > 0");
            } catch (NumberFormatException e) {
                System.out.println("❌ Enter valid number");
            }
        }
    }

    private String getValidPriority() {
        while (true) {
            System.out.print("Priority (HIGH/NORMAL/LOW) [NORMAL]: ");
            String priority = scanner.nextLine().trim();
            if (priority.isEmpty()) return "NORMAL";
            if (PrintJob.isValidPriority(priority)) return priority.toUpperCase();
            System.out.println("❌ Invalid priority");
        }
    }

    private void serveJob() {
        System.out.println("\n--- Serve Next Job ---");
        if (service.isEmpty()) {
            System.out.println("📭 No jobs to serve");
            return;
        }

        System.out.println("Next: " + service.getNextJob().toString());
        System.out.print("Serve this job? (y/n): ");
        if (scanner.nextLine().trim().toLowerCase().startsWith("y")) {
            System.out.println(service.serveNextJob());
        } else {
            System.out.println("❌ Cancelled");
        }
    }

    private void displayAllJobs() {
        System.out.println("\n--- All Pending Jobs ---");
        if (service.isEmpty()) {
            System.out.println("📭 No pending jobs");
            return;
        }

        PrintJob[] jobs = service.getAllJobs();
        System.out.println("📊 Total: " + jobs.length + " jobs");
        System.out.println("-".repeat(40));
        for (int i = 0; i < jobs.length; i++) {
            System.out.println((i + 1) + ". " + jobs[i].toString());
        }
    }

    private void searchJobs() {
        System.out.println("\n--- Search by Filename ---");
        if (service.isEmpty()) {
            System.out.println("📭 No jobs to search");
            return;
        }

        System.out.print("Filename: ");
        String filename = scanner.nextLine().trim();
        if (filename.isEmpty()) {
            System.out.println("❌ Filename cannot be empty");
            return;
        }

        PrintJob[] matches = service.searchByFileName(filename);
        if (matches.length > 0) {
            System.out.println("🔍 Found " + matches.length + " match(es):");
            for (int i = 0; i < matches.length; i++) {
                System.out.println((i + 1) + ". " + matches[i].toString());
            }
        } else {
            System.out.println("❌ No matches for: " + filename);
        }
    }

    private void displayStatistic() {
        System.out.println("\n" + "╔" + "═".repeat(42) + "╗");
        System.out.println("║         Queue Statistic           ║");
        System.out.println("╚" + "═".repeat(42) + "╝");

        System.out.println("\n📊 BASIC STATS");
        System.out.println("-".repeat(45));
        System.out.println(service.getBasicStats());

        System.out.println("\n💾 CAPACITY");
        System.out.println("-".repeat(45));
        double capacityPct = service.getCapacityPercentage();
        String capacityBar = createProgressBar(capacityPct, 30);
        String status = capacityPct > 80 ? "🔴 HIGH" : capacityPct > 60 ? "🟡 MEDIUM" : "🟢 LOW";
        System.out.printf("Usage: %s %.1f%% %s\n", capacityBar, capacityPct, status);

        System.out.println("\n🎯 PRIORITY DISTRIBUTION");
        System.out.println("-".repeat(45));
        double[] dist = service.getPriorityDistribution();
        System.out.printf("🔴 HIGH  : %s %.1f%%\n", createProgressBar(dist[0], 20), dist[0]);
        System.out.printf("🟡 NORMAL: %s %.1f%%\n", createProgressBar(dist[1], 20), dist[1]);
        System.out.printf("🟢 LOW   : %s %.1f%%\n", createProgressBar(dist[2], 20), dist[2]);

        if (!service.isEmpty()) {
            System.out.println("\n⏭️ NEXT JOB");
            System.out.println("-".repeat(45));
            System.out.println(service.getNextJob());
        }
    }

    private void displayHealthMonitor() {
        System.out.println("\n--- Queue Health Monitor ---");

        int health = service.getHealthScore();
        String healthColor = health >= 80 ? "🟢" : health >= 60 ? "🟡" : "🔴";
        String healthBar = createProgressBar(health, 25);
        String healthStatus = health >= 80 ? "Excellent" : health >= 60 ? "Good" : "Needs Attention";
        System.out.printf("🏥 Health: %s %s %d/100 (%s)\n", healthColor, healthBar, health, healthStatus);
        double waitTime = service.getAverageWaitingTime();
        System.out.printf("⏱️ Wait Time: %.1f second \n", waitTime);
        System.out.printf("📊 Queue Size: %d jobs\n", service.getAllJobs().length);
    }
    public void run() {
        System.out.println("🎉 Welcome to Print Job Manager System!");

        while (true) {
            displayMenu();

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1:
                        submitJob();
                        break;
                    case 2:
                        serveJob();
                        break;
                    case 3:
                        displayAllJobs();
                        break;
                    case 4:
                        searchJobs();
                        break;
                    case 5:
                        displayStatistic(); // Simplified version
                        break;
                    case 6:
                        displayHealthMonitor();
                        break;
                    case 7:
                        System.out.println("👋 Thank you for using Print Job Manager System!");
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("❌ Invalid option! Please select 1-7.");

                }

            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            }
        }
    }
}