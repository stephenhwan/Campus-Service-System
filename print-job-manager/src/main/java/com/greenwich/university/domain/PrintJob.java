package com.greenwich.university.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrintJob {
    private static int nextId = 1;
    private final int jobId;
    private final String fileName;
    private final int pages;
    private final String priority;
    private final LocalDateTime submissionTime;
    private LocalDateTime dequeueTime;

    public PrintJob(String fileName, int pages, String priority) {
        this.jobId = nextId++;
        this.fileName = fileName;
        this.pages = pages;
        this.priority = priority.toUpperCase();
        this.submissionTime = LocalDateTime.now(); // Tự động lấy thời gian hiện tại
        this.dequeueTime = null;
    }


    public String getPriority() {
        return priority;
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }
    public LocalDateTime getDequeueTime() { return dequeueTime; }
    public void setDequeueTime(LocalDateTime time) {this.dequeueTime = time;}
    public int getPriorityValue() {
        switch (priority) {
            case "HIGH": return 3;
            case "NORMAL": return 2;
            case "LOW": return 1;
            default: return 2;
        }
    }

    public static boolean isValidPriority(String priority) {
        String p = priority.toUpperCase();
        return p.equals("HIGH") || p.equals("NORMAL") || p.equals("LOW");
    }

    @Override
    public String toString() {
        return String.format("JobID %d: fileName:%s - Pages: (%d pages) - priority:%s [%s]",
                jobId, fileName, pages, priority,
                submissionTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    public boolean matchesFileName(String searchTerm) {
        return fileName.toLowerCase().contains(searchTerm.toLowerCase());
    }
}