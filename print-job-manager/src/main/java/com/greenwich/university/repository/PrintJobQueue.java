package com.greenwich.university.repository;

import com.greenwich.university.domain.PrintJob;

/**
 * Repository layer - Custom priority queue implementation for print jobs
 * Uses array-based heap structure for priority ordering
 */
public class PrintJobQueue {
    private PrintJob[] jobs;
    private int size;
    private int capacity;

    public PrintJobQueue(int capacity) {
        this.capacity = capacity;
        this.jobs = new PrintJob[capacity];
        this.size = 0;
    }

    /**
     * Add a job to the queue (priority-based insertion)
     */
    public boolean enqueue(PrintJob job) {
        if (size >= capacity) {
            return false;
        }

        jobs[size] = job;
        size++;

        // Bubble up to maintain priority order
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

        // Move last element to root
        jobs[0] = jobs[size - 1];
        jobs[size - 1] = null;
        size--;

        // Restore heap property
        if (size > 0) {
            bubbleDown(0);
        }

        return result;
    }

    /**
     * View the next job without removing it
     */
    public PrintJob peek() {
        if (isEmpty()) {
            return null;
        }
        return jobs[0];
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
        for (int i = 0; i < size; i++) {
            result[i] = jobs[i];
        }
        return result;
    }

    /**
     * Search for jobs by file name
     */
    public PrintJob[] searchByFileName(String fileName) {
        DynamicArray matches = new DynamicArray();

        for (int i = 0; i < size; i++) {
            if (jobs[i].matchesFileName(fileName)) {
                matches.add(jobs[i]);
            }
        }

        return matches.toArray();
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

    /**
     * Bubble up element to maintain heap property
     */
    private void bubbleUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;

            if (compareJobs(jobs[index], jobs[parentIndex]) <= 0) {
                break;
            }

            // Swap with parent
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

    /**
     * Clear all jobs
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            jobs[i] = null;
        }
        size = 0;
    }
}

/**
 * Custom dynamic array implementation for search results
 */
class DynamicArray {
    private PrintJob[] items;
    private int size;
    private int capacity;

    public DynamicArray() {
        this.capacity = 10;
        this.items = new PrintJob[capacity];
        this.size = 0;
    }

    public void add(PrintJob job) {
        if (size >= capacity) {
            resize();
        }
        items[size++] = job;
    }

    public PrintJob[] toArray() {
        PrintJob[] result = new PrintJob[size];
        for (int i = 0; i < size; i++) {
            result[i] = items[i];
        }
        return result;
    }

    private void resize() {
        capacity *= 2;
        PrintJob[] newItems = new PrintJob[capacity];
        for (int i = 0; i < size; i++) {
            newItems[i] = items[i];
        }
        items = newItems;
    }
}

