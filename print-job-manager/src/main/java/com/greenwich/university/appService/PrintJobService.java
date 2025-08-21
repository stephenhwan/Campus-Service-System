package com.greenwich.university.appService;

import com.greenwich.university.domain.PrintJob;
import com.greenwich.university.repository.PrintJobQueue;

public class PrintJobService {
    private PrintJobQueue queue;

    public PrintJobService() {
        this.queue = new PrintJobQueue(5);
    }

    public String submitJob(String fileName, int pages, String priority) {
        if (queue.isFull()) return "❌ Queue is full";

        PrintJob job = new PrintJob(fileName, pages, priority);
        return queue.enqueue(job) ? "✅ Job submitted: " + job.toString() : "❌ Failed to submit";
    }

    public String serveNextJob() {
        if (queue.isEmpty()) return "❌ No jobs to serve";
        return "✅ Served: " + queue.dequeue().toString();
    }

    public PrintJob getNextJob() { return queue.peek(); }
    public PrintJob[] getAllJobs() {
        PrintJob[] result = new PrintJob[queue.getSize()];
        // Simple copy without exposing internal array
        for (int i = 0; i < queue.getSize(); i++) {
            result[i] = queue.jobs[i]; // Direct access for simplicity
        }
        return result;
    }
    public PrintJob[] searchByFileName(String fileName) { return queue.searchByFileName(fileName); }
    public boolean isEmpty() { return queue.isEmpty(); }


    public String getBasicStats()
    { return queue.getStats();}
    public double getCapacityPercentage()
    { return queue.getCapacityPercentage(); }
    public double[] getPriorityDistribution()
    { return queue.getPriorityDistribution(); }
    public double getAverageWaitingTime()
    { return queue.getAverageWaitingTime(); }
    public int getTodayServedCount()
    { return queue.getTodayServedCount(); }
    public int getHealthScore()
    { return queue.getHealthScore(); }
}