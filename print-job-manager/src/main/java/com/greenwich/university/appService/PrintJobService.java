package com.greenwich.university.appService;

import com.greenwich.university.domain.PrintJob;
import com.greenwich.university.repository.PrintJobQueue;

/**
 * Application Service - Orchestrates use cases, delegates to domain/repository
 */
public class PrintJobService {
    private PrintJobQueue printQueue;

    public PrintJobService() {
        this.printQueue = new PrintJobQueue(100);
    }

    // ===== CORE USE CASES =====

    public String submitJob(String fileName, int pages, String priority) {
        PrintJob job = new PrintJob(fileName, pages, priority);

        if (printQueue.isFull()) {
            return "Queue is full";
        }

        if (printQueue.enqueue(job)) {
            return "Job submitted: " + job.toString();
        }
        return "Failed to submit job";
    }

    public String serveNextJob() {
        if (printQueue.isEmpty()) {
            return "No jobs to serve";
        }
        PrintJob job = printQueue.dequeue();
        return "Served: " + job.toString();
    }

    public PrintJob getNextJob() {
        return printQueue.peek();
    }

    public PrintJob[] getAllJobs() {
        return printQueue.getAllJobs();
    }

    public PrintJob[] searchByFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return new PrintJob[0];
        }
        return printQueue.searchByFileName(fileName.trim());
    }

    public boolean isEmpty() {
        return printQueue.isEmpty();
    }

    // ===== STATISTICS USE CASES - Delegate to repository =====

    public String getBasicQueueStats() {
        var count = printQueue.getPriorityCount();
        return String.format("Jobs: %d/%d | HIGH: %d | NORMAL: %d | LOW: %d",
                printQueue.getSize(), printQueue.getCapacity(),
                count.high, count.normal, count.low);
    }

    public double getCapacityPercentage() {
        return printQueue.getCapacityPercentage();
    }

    public PrintJobQueue.PriorityDistribution getPriorityDistribution() {
        return printQueue.getPriorityDistribution();
    }

    public double getAverageWaitingTime() {
        return printQueue.getAverageWaitingTime();
    }

    public int getTodayServedCount() {
        return printQueue.getTodayServedCount();
    }

    // Simplified - removed bottleneck detection and recommendations
    public int getQueueHealthScore() {
        return printQueue.calculateHealthScore();
    }
}