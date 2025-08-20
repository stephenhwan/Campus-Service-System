package com.greenwich.university.repository;

/**
 * Value object for priority statistics
 */
public class PriorityCount {
    public final int high;
    public final int normal;
    public final int low;

    public PriorityCount(int high, int normal, int low) {
        this.high = high;
        this.normal = normal;
        this.low = low;
    }
}