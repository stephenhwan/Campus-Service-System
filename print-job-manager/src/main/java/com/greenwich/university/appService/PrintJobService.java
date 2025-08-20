package com.greenwich.university.appService;

import com.greenwich.university.domain.PrintJob;
import com.greenwich.university.repository.PrintJobQueue;

/**
 * Application Service - Business logic for print job operations
 */
public class PrintJobService {
    private PrintJobQueue printQueue;

    public PrintJobService() {
        this.printQueue = new PrintJobQueue(100);
    }

    public String submitJob(String fileName, int pages, String priority) {
        // Create and add job - validation moved to UI layer for better UX
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

    public String getQueueStats() {
        var count = printQueue.getPriorityCount();
        return String.format("Jobs: %d/%d | HIGH: %d | NORMAL: %d | LOW: %d",
                printQueue.getSize(), printQueue.getCapacity(),
                count.high, count.normal, count.low);
    }

    public boolean isEmpty() {
        return printQueue.isEmpty();
    }
}