package com.greenwich.university.LostAndFound;

// Custom implementation of Linked List (no Java collections used)
class CustomLinkedList {
    Node head;

    // Add item at the end
    public void add(Item item) {
        Node newNode = new Node(item);
        if (head == null) {
            head = newNode;
            return;
        }
        Node current = head;
        while (current.next != null) {
            current = current.next;
        }
        current.next = newNode;
    }

    // Remove item by ID
    public boolean remove(int id) {
        if (head == null) return false;

        if (head.data.id == id) {
            head = head.next;
            return true;
        }

        Node current = head;
        while (current.next != null && current.next.data.id != id) {
            current = current.next;
        }

        if (current.next == null) return false;

        current.next = current.next.next;
        return true;
    }

    // Search item by keyword
    public void search(String keyword) {
        Node current = head;
        boolean found = false;
        while (current != null) {
            if (current.data.description.toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(current.data);
                found = true;
            }
            current = current.next;
        }
        if (!found) System.out.println("No item found with keyword: " + keyword);
    }

    // Display all items
    public void display() {
        if (head == null) {
            System.out.println("No items available.");
            return;
        }
        Node current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }
}
