package com.greenwich.university.repository;

import com.greenwich.university.domain.PrintJob;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PrintJobQueue {
    private PrintJob[] jobs;
    private int size;
    private int capacity;

    // Analytics data - managed at repository level
    private PrintJob[] servedJobs;
    private int servedJobsSize;
    private int servedJobsCapacity;

    public PrintJobQueue(int capacity) {
        this.capacity = capacity;
        this.jobs = new PrintJob[capacity];
        this.size = 0;

        // Initialize served jobs tracking
        this.servedJobsCapacity = 1000;
        this.servedJobs = new PrintJob[servedJobsCapacity];
        this.servedJobsSize = 0;
    }

    // ===== CORE QUEUE OPERATIONS =====

    /**
     * Add a job to the queue (priority-based insertion)
     */
    public boolean enqueue(PrintJob job) {
        if (size >= capacity) {
            return false;
        }

        jobs[size] = job;
        size++;
        bubbleUp(size - 1);
        return true;
    }

    /**
     * Remove and return the highest priority job
     */
    public PrintJob dequeue() {
        if (isEmpty()) {
            return null;
        }

        PrintJob result = jobs[0];

        // Track served job at repository level
        trackServedJob(result);

        jobs[0] = jobs[size - 1];
        jobs[size - 1] = null;
        size--;

        if (size > 0) {
            bubbleDown(0);
        }

        return result;
    }

    /**
     * Track served job - Repository responsibility
     */
    private void trackServedJob(PrintJob job) {
        if (servedJobsSize < servedJobsCapacity) {
            servedJobs[servedJobsSize] = job;
            servedJobsSize++;
        } else {
            // Sliding window - remove oldest
            for (int i = 0; i < servedJobsCapacity - 1; i++) {
                servedJobs[i] = servedJobs[i + 1];
            }
            servedJobs[servedJobsCapacity - 1] = job;
        }
    }

    /**
     * View the next job without removing it
     */
    public PrintJob peek() {
        return isEmpty() ? null : jobs[0];
    }

    /**
     * Check if queue is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Check if queue is full
     */
    public boolean isFull() {
        return size >= capacity;
    }

    /**
     * Get current size
     */
    public int getSize() {
        return size;
    }

    /**
     * Get capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Get all jobs in current order (for display purposes)
     */
    public PrintJob[] getAllJobs() {
        PrintJob[] result = new PrintJob[size];
        System.arraycopy(jobs, 0, result, 0, size);
        return result;
    }

    /**
     * Search for jobs by file name
     */
    public PrintJob[] searchByFileName(String fileName) {
        // Count matches first
        int matchCount = 0;
        for (int i = 0; i < size; i++) {
            if (jobs[i].matchesFileName(fileName)) {
                matchCount++;
            }
        }

        // Create result array with exact size
        PrintJob[] matches = new PrintJob[matchCount];
        int index = 0;
        for (int i = 0; i < size; i++) {
            if (jobs[i].matchesFileName(fileName)) {
                matches[index++] = jobs[i];
            }
        }

        return matches;
    }

    /**
     * Get jobs count by priority
     */
    public PriorityCount getPriorityCount() {
        int highCount = 0, normalCount = 0, lowCount = 0;

        for (int i = 0; i < size; i++) {
            switch (jobs[i].getPriority()) {
                case "HIGH": highCount++; break;
                case "NORMAL": normalCount++; break;
                case "LOW": lowCount++; break;
            }
        }

        return new PriorityCount(highCount, normalCount, lowCount);
    }

    // ===== ANALYTICS METHODS - Repository Level =====

    /**
     * Get capacity usage percentage
     */
    public double getCapacityPercentage() {
        return (double) size / capacity * 100;
    }

    /**
     * Get priority distribution percentages
     */
    public PriorityDistribution getPriorityDistribution() {
        PriorityCount count = getPriorityCount();
        int total = size;

        if (total == 0) {
            return new PriorityDistribution(0, 0, 0);
        }

        double highPct = (double) count.high / total * 100;
        double normalPct = (double) count.normal / total * 100;
        double lowPct = (double) count.low / total * 100;

        return new PriorityDistribution(highPct, normalPct, lowPct);
    }

    /**
     * Get today's served jobs count
     */
    public int getTodayServedCount() {
        LocalDate today = LocalDate.now();
        int count = 0;

        for (int i = 0; i < servedJobsSize; i++) {
            if (servedJobs[i] != null &&
                    servedJobs[i].getSubmissionTime().toLocalDate().equals(today)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Get average waiting time for current jobs (in minutes)
     */
    public double getAverageWaitingTime() {
        if (isEmpty()) return 0;

        LocalDateTime now = LocalDateTime.now();
        double totalMinutes = 0;

        for (int i = 0; i < size; i++) {
            long minutes = java.time.Duration.between(jobs[i].getSubmissionTime(), now).toMinutes();
            totalMinutes += minutes;
        }

        return totalMinutes / size;
    }

    /**
     * Calculate simple health score based on queue metrics
     */
    public int calculateHealthScore() {
        double capacityPct = getCapacityPercentage();
        double avgWaitTime = getAverageWaitingTime();
        PriorityDistribution dist = getPriorityDistribution();

        int score = 100;

        // Capacity penalty
        if (capacityPct > 90) score -= 30;
        else if (capacityPct > 70) score -= 15;

        // Wait time penalty
        if (avgWaitTime > 60) score -= 25;
        else if (avgWaitTime > 30) score -= 10;

        // Priority imbalance penalty
        if (dist.highPct > 60) score -= 15;

        return Math.max(0, score);
    }

    // ===== HEAP OPERATIONS =====

    /**
     * Bubble up element to maintain heap property
     */
    private void bubbleUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;

            if (compareJobs(jobs[index], jobs[parentIndex]) <= 0) {
                break;
            }

            swap(index, parentIndex);
            index = parentIndex;
        }
    }

    /**
     * Bubble down element to maintain heap property
     */
    private void bubbleDown(int index) {
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int largest = index;

            if (leftChild < size && compareJobs(jobs[leftChild], jobs[largest]) > 0) {
                largest = leftChild;
            }

            if (rightChild < size && compareJobs(jobs[rightChild], jobs[largest]) > 0) {
                largest = rightChild;
            }

            if (largest == index) {
                break;
            }

            swap(index, largest);
            index = largest;
        }
    }

    /**
     * Compare two jobs (positive if job1 has higher priority than job2)
     */
    private int compareJobs(PrintJob job1, PrintJob job2) {
        // First compare by priority
        int priorityDiff = job1.getPriorityValue() - job2.getPriorityValue();
        if (priorityDiff != 0) {
            return priorityDiff;
        }

        // If same priority, earlier submission time has higher priority
        return job2.getSubmissionTime().compareTo(job1.getSubmissionTime());
    }

    /**
     * Swap two elements in the array
     */
    private void swap(int i, int j) {
        PrintJob temp = jobs[i];
        jobs[i] = jobs[j];
        jobs[j] = temp;
    }

    // ===== VALUE OBJECTS =====

    /**
     * Priority distribution percentages
     */
    public static class PriorityDistribution {
        public final double highPct;
        public final double normalPct;
        public final double lowPct;

        public PriorityDistribution(double highPct, double normalPct, double lowPct) {
            this.highPct = highPct;
            this.normalPct = normalPct;
            this.lowPct = lowPct;
        }
    }
}