package com.greenwich.university;

// Represents an item in the system
class Item {
    int id;                 // Unique identifier
    String description;     // e.g. "Red Umbrella"
    String date;            // e.g. "2025-08-16"
    String type;            // "Lost" or "Found"

    public Item(int id, String description, String date, String type) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.type = type;
    }

    @Override
    public String toString() {
        return "ID: " + id + " | " + type + " | " + description + " | Date: " + date;
    }
}
