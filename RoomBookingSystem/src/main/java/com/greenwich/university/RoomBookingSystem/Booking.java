package com.greenwich.university.RoomBookingSystem;

// Importing classes from java.time package to handle dates and times
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// The Booking class represents a single room booking
public class Booking {
    // Unique identifier for each booking
    int id;
    // Room code or name (e.g., "A101")
    String room;
    // Date of the booking
    LocalDate date;
    // Starting time of the booking (24-hour format)
    LocalTime start;
    // Ending time of the booking (24-hour format)
    LocalTime end;
    // Name of the person who booked the room
    String bookedBy;

    // Constructor: initializes a booking with its details
    public Booking(int id, String room, LocalDate date, LocalTime start, LocalTime end, String bookedBy) {
        this.id = id;               // Set unique booking ID
        this.room = room;           // Set room name or number
        this.date = date;           // Set booking date
        this.start = start;         // Set starting time
        this.end = end;             // Set ending time
        this.bookedBy = bookedBy;   // Set the name of the person booking
    }

    // Method to create a combined date-time key (for sorting/comparison)
    LocalDateTime key() {
        return LocalDateTime.of(date, start);
    }

    // Method to return a formatted string describing the booking
    @Override
    public String toString() {
        return String.format("#%d | Room %s | %s %s-%s | By: %s",
                id,                // Booking ID
                room,              // Room name/number
                date,              // Booking date
                start,             // Starting time
                end,               // Ending time
                (bookedBy == null ? "-" : bookedBy)); // Show "-" if bookedBy is null
    }
}
