package com.greenwich.university.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Domain entity representing a print job
 */
public class PrintJob {
    private static int nextId = 1;

    private final int jobId;
    private final String fileName;
    private final int pages;
    private final String priority;

    private final LocalDateTime submissionTime;

    public PrintJob(String fileName, int pages, String priority, LocalDateTime submissionTime) {
        this.jobId = nextId++;
        this.fileName = fileName;
        this.pages = pages;
        this.priority = priority.toUpperCase();
        this.submissionTime = LocalDateTime.now();
    }

    // Getters
    public int getJobId() {
        return jobId;
    }

    public String getFileName() {
        return fileName;
    }

    public int getPages() {
        return pages;
    }

    public String getPriority() {
        return priority;
    }


    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    /**
     * Get priority value for comparison (higher number = higher priority)
     */
    public int getPriorityValue() {
        switch (priority) {
            case "HIGH": return 3;
            case "NORMAL": return 2;
            case "LOW": return 1;
            default: return 2;
        }
    }

    /**
     * Check if priority is valid
     */
    public static boolean isValidPriority(String priority) {
        String p = priority.toUpperCase();
        return p.equals("HIGH") || p.equals("NORMAL") || p.equals("LOW");
    }

    /**
     * Get basic string representation
     */
    @Override
    public String toString() {
        return String.format("Job#%d: %s (%d pages) - %s [%s]",
                jobId, fileName, pages, priority);
    }

    /**
     * Get detailed information
     */
    public String getDetailedInfo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format(
                "Job ID: %d\n" +
                        "File Name: %s\n" +
                        "Pages: %d\n" +
                        "Priority: %s\n" +
                        "Submitted: %s",
                jobId, fileName, pages, priority
                submissionTime.format(formatter)
        );
    }

    /**
     * Check if this job matches the file name search criteria
     */
    public boolean matchesFileName(String searchTerm) {
        return fileName.toLowerCase().contains(searchTerm.toLowerCase());
    }

    /**
     * Check if this job matches the user search criteria
     */

}