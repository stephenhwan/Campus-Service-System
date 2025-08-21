package com.greenwich.university.ui;
import com.greenwich.university.appService.PrintJobService;
import com.greenwich.university.repository.PrintJobQueue.PriorityDistribution;
import com.greenwich.university.domain.PrintJob;
import java.util.Scanner;
/**
 * UI Layer - Handles user interaction and presentation
 * Simplified analytics without bottleneck detection and recommendations
 */
public class PrintJobManager {
    private PrintJobService printJobService;
    private Scanner scanner;
    public PrintJobManager() {
        this.printJobService = new PrintJobService();
        this.scanner = new Scanner(System.in);
    }
    public void displayMenu() {
        System.out.println("\n=============================================");
        System.out.println("       PRINT JOB MANAGER SYSTEM       ");
        System.out.println("=============================================");
        System.out.println("---------------------------------------------");
        System.out.println("             QUEUE STATISTIC            ");
        String stats = printJobService.getBasicQueueStats();
        System.out.println("📊 " + stats);

        // Show capacity bar
        displayCapacityBar();

        if (!printJobService.isEmpty()) {
            PrintJob nextJob = printJobService.getNextJob();
            System.out.println("⭐️ Next job: " + nextJob.toString());
        } else {
            System.out.println("📭 No jobs in queue");
        }
        System.out.println("---------------------------------------------");
        System.out.println("1. Submit a new print job");
        System.out.println("2. Serve the next print job in queue");
        System.out.println("3. Display all pending print jobs");
        System.out.println("4. Search for a job by file name");
        System.out.println("5. Display advanced queue analytics");
        System.out.println("6. Queue Health Monitor");
        System.out.println("7. Exit");
        System.out.println("=============================================");
        System.out.print("Please select an option (1-7): ");
    }
    /**
     * Display simple capacity progress bar
     */
    private void displayCapacityBar() {
        double capacityPct = printJobService.getCapacityPercentage();
        String bar = createProgressBar(capacityPct, 20);
        String color = capacityPct > 80 ? "🔴" : capacityPct > 60 ? "🟡" : "🟢";
        System.out.printf("💾 Capacity: %s %s %.1f%%\n", color, bar, capacityPct);
    }
    /**
     * Create visual progress bar
     */
    private String createProgressBar(double percentage, int width) {
        int filled = (int) (percentage * width / 100);
        StringBuilder bar = new StringBuilder("[");

        for (int i = 0; i < width; i++) {
            if (i < filled) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("]");
        return bar.toString();
    }
    public void submitPrintJob() {
        System.out.println("\n--- Submit New Print Job ---");
        System.out.print("File name: ");
        String fileName = scanner.nextLine().trim();
        int pages = getValidPages();
        String priority = getValidPriority();
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
            if (PrintJob.isValidPriority(priority)) {
                return priority.toUpperCase();
            }
            System.out.println("❌ Invalid priority. Please enter HIGH, NORMAL, or LOW.");
        }
    }
    public void serveNextJob() {
        System.out.println("\n--- Serve Next Print Job ---");
        if (printJobService.isEmpty()) {
            System.out.println("📭 No jobs in queue to serve.");
            return;
        }
        PrintJob nextJob = printJobService.getNextJob();
        System.out.println("Next job to be served:");
        System.out.println(nextJob.toString());
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
    /**
     * Display advanced queue analytics (Case 5)
     */
    public void displayQueueStatistic() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║          ADVANCED QUEUE ANALYTICS       ║");
        System.out.println("╚══════════════════════════════════════════╝");

        // Basic Statistics
        String basicStats = printJobService.getBasicQueueStats();
        System.out.println("\n📊 BASIC STATISTICS");
        System.out.println("─".repeat(50));
        System.out.println(basicStats);

        // Capacity Analysis with Progress Bar
        System.out.println("\n💾 CAPACITY ANALYSIS");
        System.out.println("─".repeat(50));
        double capacityPct = printJobService.getCapacityPercentage();
        String capacityBar = createProgressBar(capacityPct, 30);
        String capacityStatus = capacityPct > 80 ? "🔴 HIGH" : capacityPct > 60 ? "🟡 MEDIUM" : "🟢 LOW";
        System.out.printf("Usage: %s %.1f%% %s\n", capacityBar, capacityPct, capacityStatus);

        // Priority Distribution with Visual Bars
        System.out.println("\n🎯 PRIORITY DISTRIBUTION");
        System.out.println("─".repeat(50));
        displayPriorityDistribution();

        // Time-based Statistics
        System.out.println("\n⏰ TIME-BASED STATISTICS");
        System.out.println("─".repeat(50));
        System.out.printf("Today's processed jobs: %d\n", printJobService.getTodayServedCount());
        System.out.printf("Average waiting time: %.1f minutes\n", printJobService.getAverageWaitingTime());

        if (!printJobService.isEmpty()) {
            PrintJob nextJob = printJobService.getNextJob();
            System.out.println("\n⏭️ NEXT JOB TO SERVE");
            System.out.println("─".repeat(50));
            System.out.println(nextJob.toString());
        }
    }

    /**
     * NEW - Queue Health Monitor (Case 6) - Simplified
     */
    public void displayQueueHealthMonitor() {
        System.out.println("\n--- Queue Health Monitor ---");

        // Health Score
        int healthScore = printJobService.getQueueHealthScore();
        String healthColor = getHealthColor(healthScore);
        String healthBar = createProgressBar(healthScore, 25);
        System.out.printf("🏥 Health Score: %s %s %d/100\n", healthColor, healthBar, healthScore);

        String healthStatus = healthScore >= 80 ? "Excellent" :
                healthScore >= 60 ? "Good" : "Needs Attention";
        System.out.println("Status: " + healthStatus);

        // Quick Health Checks
        double capacityPct = printJobService.getCapacityPercentage();
        String capacityStatus = capacityPct > 80 ? "🔴 High" : capacityPct > 60 ? "🟡 Medium" : "🟢 Low";
        System.out.printf("💾 Capacity Usage: %.1f%% %s\n", capacityPct, capacityStatus);

        double avgWaitTime = printJobService.getAverageWaitingTime();
        String waitStatus = avgWaitTime > 20 ? "🔴 Long" : avgWaitTime > 10 ? "🟡 Moderate" : "🟢 Good";
        System.out.printf("⏱️ Average Wait: %.1f min %s\n", avgWaitTime, waitStatus);

        System.out.printf("📊 Queue Size: %d jobs\n", printJobService.getAllJobs().length);
    }

    /**
     * Display priority distribution with visual bars
     */
    private void displayPriorityDistribution() {
        PriorityDistribution dist = printJobService.getPriorityDistribution();

        System.out.printf("🔴 HIGH   : %s %.1f%%\n",
                createProgressBar(dist.highPct, 20), dist.highPct);
        System.out.printf("🟡 NORMAL : %s %.1f%%\n",
                createProgressBar(dist.normalPct, 20), dist.normalPct);
        System.out.printf("🟢 LOW    : %s %.1f%%\n",
                createProgressBar(dist.lowPct, 20), dist.lowPct);
    }

    /**
     * Get health status color emoji
     */
    private String getHealthColor(int healthScore) {
        if (healthScore >= 80) return "🟢";
        if (healthScore >= 60) return "🟡";
        return "🔴";
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
                        displayQueueStatistic(); // Simplified version
                        break;
                    case 6:
                        displayQueueHealthMonitor();
                    case 7:
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