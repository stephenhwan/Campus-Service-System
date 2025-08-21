package com.greenwich.university;

// System manager class for Lost and Found Tracker
class LostAndFoundSystem {
    private CustomLinkedList items;
    private int idCounter = 1;

    public LostAndFoundSystem() {
        items = new CustomLinkedList();
    }

    // Add new lost/found item
    public void addItem(String description, String date, String type) {
        Item item = new Item(idCounter++, description, date, type);
        items.add(item);
        System.out.println("Item added successfully: " + item);
    }

    // View all unclaimed items
    public void viewItems() {
        items.display();
    }

    // Claim (remove) item
    public void claimItem(int id) {
        boolean removed = items.remove(id);
        if (removed) {
            System.out.println("Item with ID " + id + " has been claimed.");
        } else {
            System.out.println("Item not found.");
        }
    }

    // Search by keyword
    public void searchItem(String keyword) {
        items.search(keyword);
    }
}
