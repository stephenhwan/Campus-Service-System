package com.greenwich.university.RoomBookingSystem;

// Import classes to represent dates and times

import java.time.LocalDate;
import java.time.LocalTime;

// BookingManager class controls all logic related to managing bookings
public class BookingManager {

    // ---------- DynamicArray (custom implementation, no Java Collections) ----------
    // Custom dynamic array to store Booking objects
    private static class DynamicArray {
        // Internal storage array, initially size 8
        private Booking[] data = new Booking[8];
        // Current number of stored elements
        private int size = 0;

        // Return the number of elements
        int size() { return size; }

        // Get element at index i
        Booking get(int i) {
            if (i < 0 || i >= size) throw new IndexOutOfBoundsException();
            return data[i];
        }

        // Replace element at index i with booking b
        void set(int i, Booking b) {
            if (i < 0 || i >= size) throw new IndexOutOfBoundsException();
            data[i] = b;
        }

        // Add a new booking at the end of the array
        void add(Booking b) {
            ensure(size + 1);       // Ensure capacity before adding
            data[size++] = b;       // Place new booking and increment size
        }

        // Remove booking at given index and return it
        Booking removeAt(int idx) {
            if (idx < 0 || idx >= size) throw new IndexOutOfBoundsException();
            Booking out = data[idx];
            // Shift all elements after idx one position left
            for (int i = idx; i < size - 1; i++) data[i] = data[i + 1];
            // Decrease size and set last element to null
            data[--size] = null;
            return out;
        }

        // Convert stored data to a new array (snapshot)
        Booking[] toArray() {
            Booking[] out = new Booking[size];
            for (int i = 0; i < size; i++) out[i] = data[i];
            return out;
        }

        // Ensure internal array has enough capacity
        private void ensure(int cap) {
            if (cap <= data.length) return;    // Already enough space
            int newCap = data.length * 2;      // Double capacity
            while (newCap < cap) newCap *= 2;  // Keep doubling if needed
            Booking[] nd = new Booking[newCap];
            // Copy old data to new array
            for (int i = 0; i < size; i++) nd[i] = data[i];
            data = nd;
        }
    }

    // ---------- Free ID pool (singly linked list) ----------
    // Node for linked list storing free IDs
    private static class IntNode {
        int v;                // ID value
        IntNode next;         // Pointer to next node
        IntNode(int v, IntNode n){ this.v=v; this.next=n; }
    }

    // Linked list–based pool for recycling booking IDs
    private static class FreeIdPool {
        private IntNode head;   // Top of linked list
        private int nextId = 1; // Next sequential ID if pool is empty

        // Acquire an ID: either from pool or generate new
        int acquire() {
            if (head != null) {
                int v = head.v;       // Reuse ID from pool
                head = head.next;     // Remove from list
                return v;
            }
            return nextId++;          // Otherwise generate new ID
        }

        // Release an ID back to the pool (push to front of list)
        void release(int id) { head = new IntNode(id, head); }
    }

    // ---------- Merge Sort by date-time ----------
    // Sorting helper class to sort bookings chronologically
    private static class BookingSorter {
        // Public method: sort an array of bookings by date/time
        static Booking[] sortByDateTime(Booking[] arr) {
            if (arr == null || arr.length <= 1) return arr;
            Booking[] tmp = new Booking[arr.length];      // Temporary buffer
            mergeSort(arr, tmp, 0, arr.length - 1);       // Recursive sort
            return arr;
        }

        // Recursive merge sort
        private static void mergeSort(Booking[] a, Booking[] tmp, int l, int r) {
            if (l >= r) return;                  // Base case: one element
            int m = (l + r) >>> 1;               // Midpoint
            mergeSort(a, tmp, l, m);             // Sort left half
            mergeSort(a, tmp, m + 1, r);         // Sort right half
            merge(a, tmp, l, m, r);              // Merge two halves
        }

        // Merge two sorted halves into one sorted section
        private static void merge(Booking[] a, Booking[] tmp, int l, int m, int r) {
            int i = l, j = m + 1, k = l;
            // Compare and merge elements from both halves
            while (i <= m && j <= r) {
                if (!a[i].key().isAfter(a[j].key())) tmp[k++] = a[i++];
                else tmp[k++] = a[j++];
            }
            // Copy remaining elements
            while (i <= m) tmp[k++] = a[i++];
            while (j <= r) tmp[k++] = a[j++];
            // Copy merged result back into original array
            for (int t = l; t <= r; t++) a[t] = tmp[t];
        }
    }

    // ---------- Manager (main controller for bookings) ----------
    private final DynamicArray storage = new DynamicArray(); // Stores all bookings
    private final FreeIdPool idPool = new FreeIdPool();      // Manages booking IDs

    /**
     * Add booking if available (no overlap in same room and date).
     * Returns the created Booking or null if conflict.
     */
    public Booking add(String room, LocalDate date, LocalTime start, LocalTime end, String by) {
        if (!start.isBefore(end)) throw new IllegalArgumentException("Start must be before end.");
        if (!isAvailable(room, date, start, end)) return null;   // Reject if not available
        int id = idPool.acquire();                               // Get new ID
        Booking b = new Booking(id, room, date, start, end, by); // Create booking
        storage.add(b);                                          // Add to storage
        return b;
    }

    /**
     * Check availability: returns true if no overlapping booking exists
     * for the same room and date.
     */
    public boolean isAvailable(String room, LocalDate date, LocalTime start, LocalTime end) {
        for (int i = 0; i < storage.size(); i++) {
            Booking b = storage.get(i);
            if (b.room.equalsIgnoreCase(room) && b.date.equals(date)) {
                // Overlap if start < existing.end AND end > existing.start
                boolean overlap = start.isBefore(b.end) && end.isAfter(b.start);
                if (overlap) return false;  // Conflict found
            }
        }
        return true;  // No conflict
    }

    /** Cancel a booking by ID. Returns true if booking was found and removed. */
    public boolean cancel(int id) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).id == id) {
                storage.removeAt(i);    // Remove booking
                idPool.release(id);     // Return ID to pool
                return true;
            }
        }
        return false;   // Booking not found
    }

    /** Return an array of bookings sorted by date and time. */
    public Booking[] listSortedByDateTime() {
        Booking[] snap = storage.toArray();            // Take snapshot of data
        return BookingSorter.sortByDateTime(snap);     // Sort snapshot
    }

    /** Optional: Find a booking by exact slot (room, date, start and end times). */
    public Booking find(String room, LocalDate date, LocalTime start, LocalTime end) {
        for (int i = 0; i < storage.size(); i++) {
            Booking b = storage.get(i);
            if (b.room.equalsIgnoreCase(room) && b.date.equals(date)
                    && b.start.equals(start) && b.end.equals(end)) return b;
        }
        return null;  // Not found
    }
}
