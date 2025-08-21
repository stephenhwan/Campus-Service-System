package com.greenwich.university.repository;

import com.greenwich.university.domain.PrintJob;
import java.time.LocalDateTime;

public class PrintJobQueue {
    public PrintJob[] jobs;
    private int size;
    private final int capacity;
    private int servedToday = 0;

    public PrintJobQueue(int capacity) {
        this.capacity = capacity;
        this.jobs = new PrintJob[capacity];
        this.size = 0;
    }

    // Core operations
    public boolean enqueue(PrintJob job) {
        if (size >= capacity) return false;
        jobs[size++] = job;
        bubbleUp(size - 1);
        return true;
    }

    public PrintJob dequeue() {
        if (isEmpty()) return null;
        PrintJob result = jobs[0];
        servedToday++;
        jobs[0] = jobs[--size];
        if (size > 0) bubbleDown(0);
        return result;
    }

    public PrintJob peek() { return isEmpty() ? null : jobs[0]; }
    public boolean isEmpty() { return size == 0; }
    public boolean isFull() { return size >= capacity; }
    public int getSize() { return size; }
    public int getCapacity() { return capacity; }

    // Search functionality
    public PrintJob[] searchByFileName(String fileName) {
        PrintJob[] temp = new PrintJob[size];
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (jobs[i].matchesFileName(fileName)) {
                temp[count++] = jobs[i];
            }
        }
        PrintJob[] result = new PrintJob[count];
        System.arraycopy(temp, 0, result, 0, count);
        return result;
    }

    // Analytics - simplified
    public String getStats() {
        int[] counts = getPriorityCounts();
        return String.format("Jobs: %d/%d | HIGH:%d NORMAL:%d LOW:%d",
                size, capacity, counts[0], counts[1], counts[2]);
    }

    public double getCapacityPercentage() {
        return (double) size / capacity * 100;
    }

    public double[] getPriorityDistribution() {
        if (size == 0) return new double[]{0, 0, 0};
        int[] counts = getPriorityCounts();
        return new double[]{
                counts[0] * 100.0 / size,
                counts[1] * 100.0 / size,
                counts[2] * 100.0 / size
        };
    }

    public double getAverageWaitingTime() {
        if (isEmpty()) return 0;
        LocalDateTime now = LocalDateTime.now();
        long totalMinutes = 0;
        for (int i = 0; i < size; i++) {
            totalMinutes += java.time.Duration.between(jobs[i].getSubmissionTime(), now).toMinutes();
        }
        return (double) totalMinutes / size;
    }

    public int getTodayServedCount() { return servedToday; }

    public int getHealthScore() {
        int score = 100;
        double capacity = getCapacityPercentage();
        double waitTime = getAverageWaitingTime();

        if (capacity > 90) score -= 30;
        else if (capacity > 70) score -= 15;

        if (waitTime > 60) score -= 25;
        else if (waitTime > 30) score -= 10;

        double[] dist = getPriorityDistribution();
        if (dist[0] > 60) score -= 15; // too many HIGH priority

        return Math.max(0, score);
    }

    private int[] getPriorityCounts() {
        int[] counts = {0, 0, 0}; // HIGH, NORMAL, LOW
        for (int i = 0; i < size; i++) {
            switch (jobs[i].getPriority()) {
                case "HIGH": counts[0]++; break;
                case "NORMAL": counts[1]++; break;
                case "LOW": counts[2]++; break;
            }
        }
        return counts;
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (compareJobs(jobs[index], jobs[parent]) <= 0) break;
            swap(index, parent);
            index = parent;
        }
    }

    private void bubbleDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int largest = index;

            if (left < size && compareJobs(jobs[left], jobs[largest]) > 0) largest = left;
            if (right < size && compareJobs(jobs[right], jobs[largest]) > 0) largest = right;
            if (largest == index) break;

            swap(index, largest);
            index = largest;
        }
    }

    private int compareJobs(PrintJob j1, PrintJob j2) {
        int diff = j1.getPriorityValue() - j2.getPriorityValue();
        return diff != 0 ? diff : j2.getSubmissionTime().compareTo(j1.getSubmissionTime());
    }

    private void swap(int i, int j) {
        PrintJob temp = jobs[i];
        jobs[i] = jobs[j];
        jobs[j] = temp;
    }
}
