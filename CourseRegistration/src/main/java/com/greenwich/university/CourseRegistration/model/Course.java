package model;

import datastructures.MyQueue;

public class Course {
    private String code;
    private String name;
    private int capacity;
    private int enrolled;
    private MyQueue<String> waitlist;

    public Course(String code, String name, int capacity) {
        this.code = code;
        this.name = name;
        this.capacity = capacity;
        this.enrolled = 0;
        this.waitlist = new MyQueue<>();
    }

    public String getCode() { return code; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public int getEnrolled() { return enrolled; }
    public MyQueue<String> getWaitlist() { return waitlist; }

    public boolean hasSpace() {
        return enrolled < capacity;
    }

    // Try to enroll a student. If full, add to waitlist.
    public boolean enroll(String studentId) {
        if (hasSpace()) {
            enrolled++;
            return true;
        } else {
            waitlist.enqueue(studentId);
            return false;
        }
    }

    /**
     * Drop a student. If waitlist exists, promote first student and return their ID.
     * @param studentId the student who dropped
     * @return promoted student ID if promotion happened, else null
     */
    public String drop(String studentId) {
        if (enrolled > 0) {
            enrolled--;
        }

        if (!waitlist.isEmpty()) {
            String promoted = waitlist.dequeue();
            enrolled++;
            return promoted;
        }

        return null;
    }

    @Override
    public String toString() {
        return code + " - " + name + " (" + enrolled + "/" + capacity + ")";
    }
}
