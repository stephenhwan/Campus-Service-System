package com.greenwich.university.appService;

import com.greenwich.university.domain.PrintJob;
import com.greenwich.university.repository.PrintJobQueue;
import com.greenwich.university.repository.PriorityCount;

/**
 * Application Service layer - Business logic for print job operations
 */
public class PrintJobService {
    private PrintJobQueue printQueue;

    public PrintJobService() {
        this.printQueue = new PrintJobQueue(100);
    }

    /**
     * Submit a new print job
     */
    public SubmissionResult submitJob(String fileName, int pages, String priority) {
        // Validation
        if (fileName == null || fileName.trim().isEmpty()) {
            return new SubmissionResult(false, "File name cannot be empty", null);
        }

        if (pages <= 0) {
            return new SubmissionResult(false, "Number of pages must be greater than 0", null);
        }

        if (!PrintJob.isValidPriority(priority)) {
            return new SubmissionResult(false, "Invalid priority. Use HIGH, NORMAL, or LOW", null);
        }

        if (printQueue.isFull()) {
            return new SubmissionResult(false, "Queue is full. Cannot accept new jobs", null);
        }

        // Create and enqueue job
        PrintJob job = new PrintJob(fileName.trim(), pages, priority);
        boolean success = printQueue.enqueue(job);

        if (success) {
            return new SubmissionResult(true, "Print job submitted successfully", job);
        } else {
            return new SubmissionResult(false, "Failed to submit job", null);
        }
    }

    /**
     * Get the next job to be served
     */
    public PrintJob getNextJob() {
        return printQueue.peek();
    }

    /**
     * Serve the next job (remove from queue)
     */
    public ServiceResult serveNextJob() {
        if (printQueue.isEmpty()) {
            return new ServiceResult(false, "No jobs in queue to serve", null);
        }

        PrintJob servedJob = printQueue.dequeue();
        return new ServiceResult(true, "Job served successfully", servedJob);
    }

    /**
     * Get all pending jobs
     */
    public PrintJob[] getAllPendingJobs() {
        return printQueue.getAllJobs();
    }

    /**
     * Search jobs by file name
     */
    public SearchResult searchByFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return new SearchResult(false, "Search term cannot be empty", new PrintJob[0]);
        }

        PrintJob[] matches = printQueue.searchByFileName(fileName.trim());
        return new SearchResult(true, "Search completed", matches);
    }



    /**
     * Get queue statistics
     */
    public QueueList getStatistics() {
        PriorityCount priorityCount = printQueue.getPriorityCount();

        return new QueueList(
                printQueue.getSize(),
                printQueue.getCapacity(),
                printQueue.getCapacity() - printQueue.getSize(),
                priorityCount.high,
                priorityCount.normal,
                priorityCount.low,
                printQueue.peek()
        );
    }

    /**
     * Check if queue is empty
     */
    public boolean isEmpty() {
        return printQueue.isEmpty();
    }
}

/**
 * Result object for job submission operations
 */
class SubmissionResult {
    public final boolean success;
    public final String message;
    public final PrintJob job;

    public SubmissionResult(boolean success, String message, PrintJob job) {
        this.success = success;
        this.message = message;
        this.job = job;
    }
}

/**
 * Result object for job service operations
 */
class ServiceResult {
    public final boolean success;
    public final String message;
    public final PrintJob job;

    public ServiceResult(boolean success, String message, PrintJob job) {
        this.success = success;
        this.message = message;
        this.job = job;
    }
}

/**
 * Result object for search operations
 */
class SearchResult {
    public final boolean success;
    public final String message;
    public final PrintJob[] matches;

    public SearchResult(boolean success, String message, PrintJob[] matches) {
        this.success = success;
        this.message = message;
        this.matches = matches;
    }
}

/**
 * Statistics about the queue
 */
class QueueList {
    public final int currentJobs;
    public final int capacity;
    public final int availableSlots;
    public final int highPriorityJobs;
    public final int normalPriorityJobs;
    public final int lowPriorityJobs;
    public final PrintJob nextJob;

    public QueueList(int currentJobs, int capacity, int availableSlots,
                           int highPriorityJobs, int normalPriorityJobs, int lowPriorityJobs,
                           PrintJob nextJob) {
        this.currentJobs = currentJobs;
        this.capacity = capacity;
        this.availableSlots = availableSlots;
        this.highPriorityJobs = highPriorityJobs;
        this.normalPriorityJobs = normalPriorityJobs;
        this.lowPriorityJobs = lowPriorityJobs;
        this.nextJob = nextJob;
    }
}