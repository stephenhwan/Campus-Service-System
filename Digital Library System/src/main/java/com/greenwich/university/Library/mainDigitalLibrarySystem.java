package com.greenwich.university.Library;
import java.util.Scanner;

public class mainDigitalLibrarySystem {
    public void run() {
        Scanner sc = new Scanner(System.in);
        Library library = new Library(100); // Max 100 books

        while (true) {
            System.out.println("\n--- Library Menu ---");
            System.out.println("1. Add Book");
            System.out.println("2. Search by Title");
            System.out.println("3. Search by Author");
            System.out.println("4. Borrow Book");
            System.out.println("5. Return Book");
            System.out.println("6. Display All Books");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(sc.nextLine());


            switch (choice) {
                case 1:
                    System.out.print("Enter title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter author: ");
                    String author = sc.nextLine();
                    library.addBook(title, author);
                    break;

                case 2:
                    System.out.print("Enter title to search: ");
                    String searchTitle = sc.nextLine();
                    library.searchByTitle(searchTitle);
                    break;

                case 3:
                    System.out.print("Enter author to search: ");
                    String searchAuthor = sc.nextLine();
                    library.searchByAuthor(searchAuthor);
                    break;

                case 4:
                    System.out.print("Enter title to borrow: ");
                    String borrowTitle = sc.nextLine();
                    library.borrowBook(borrowTitle);
                    break;

                case 5:
                    System.out.print("Enter title to return: ");
                    String returnTitle = sc.nextLine();
                    library.returnBook(returnTitle);
                    break;

                case 6:
                    library.displayAllBooks();
                    break;

                case 7:
                    System.out.println("Exiting the system. Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
class Book {
    String title;
    String author;
    boolean isAvailable;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isAvailable = true; // Available by default
    }

    public void borrowBook() {
        if (isAvailable) {
            isAvailable = false;
            System.out.println("Book borrowed successfully.");
        } else {
            System.out.println("Book is currently unavailable.");
        }
    }

    public void returnBook() {
        isAvailable = true;
        System.out.println("Book returned successfully.");
    }

    public void display() {
        System.out.println("Title: " + title + ", Author: " + author + ", Status: " + (isAvailable ? "Available" : "Unavailable"));
    }
}
class Library {
    private Book[] books;
    private int count;

    public Library(int size) {
        books = new Book[size];
        count = 0;
    }

    public void addBook(String title, String author) {
        if (count >= books.length) {
            System.out.println("Library is full. Cannot add more books.");
            return;
        }
        books[count++] = new Book(title, author);
        System.out.println("Book added successfully.");
    }

    public void searchByTitle(String title) {
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (books[i].title.equalsIgnoreCase(title)) {
                books[i].display();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No book found with the given title.");
        }
    }

    public void searchByAuthor(String author) {
        boolean found = false;
        for (int i = 0; i < count; i++) {
            if (books[i].author.equalsIgnoreCase(author)) {
                books[i].display();
                found = true;
            }
        }
        if (!found) {
            System.out.println("No book found with the given author.");
        }
    }

    public void borrowBook(String title) {
        for (int i = 0; i < count; i++) {
            if (books[i].title.equalsIgnoreCase(title)) {
                books[i].borrowBook();
                return;
            }
        }
        System.out.println("Book not found.");
    }

    public void returnBook(String title) {
        for (int i = 0; i < count; i++) {
            if (books[i].title.equalsIgnoreCase(title)) {
                books[i].returnBook();
                return;
            }
        }
        System.out.println("Book not found.");
    }

    public void displayAllBooks() {
        if (count == 0) {
            System.out.println("Library is empty.");
        }
        for (int i = 0; i < count; i++) {
            books[i].display();
        }
    }
}

