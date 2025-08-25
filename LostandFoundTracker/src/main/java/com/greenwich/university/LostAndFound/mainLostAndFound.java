package com.greenwich.university.LostAndFound;

import java.util.Scanner;

public class mainLostAndFound {
    private Scanner scanner = new Scanner(System.in);
    private LostAndFoundSystem system = new LostAndFoundSystem();

    public void run() {
        System.out.println("📋 Welcome to Lost and Found Tracker System!");

        while (true) {
            displayMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());

                switch (choice) {
                    case 1:
                        addItem();
                        break;
                    case 2:
                        viewAllItems();
                        break;
                    case 3:
                        searchItem();
                        break;
                    case 4:
                        claimItem();
                        break;
                    case 5:
                        System.out.println("👋 Thank you for using Lost and Found Tracker!");
                        return;
                    default:
                        System.out.println("❌ Invalid option! Please select 1-5.");
                }

                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();

            } catch (NumberFormatException e) {
                System.out.println("❌ Please enter a valid number!");
            } catch (Exception e) {
                System.err.println("❌ Error occurred: " + e.getMessage());
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n" + "=".repeat(45));
        System.out.println("       LOST AND FOUND TRACKER");
        System.out.println("=".repeat(45));
        System.out.println("1. 📝 Add Item (Lost/Found)");
        System.out.println("2. 👀 View All Items");
        System.out.println("3. 🔍 Search Items");
        System.out.println("4. ✅ Claim Item");
        System.out.println("5. 🚪 Exit");
        System.out.println("=".repeat(45));
        System.out.print("Select option (1-5): ");
    }

    private void addItem() {
        System.out.println("\n--- Add New Item ---");

        System.out.print("Description: ");
        String description = scanner.nextLine().trim();

        System.out.print("Date (YYYY-MM-DD): ");
        String date = scanner.nextLine().trim();

        System.out.print("Type (Lost/Found): ");
        String type = scanner.nextLine().trim();

        system.addItem(description, date, type);
    }

    private void viewAllItems() {
        System.out.println("\n--- All Items ---");
        system.viewItems();
    }

    private void searchItem() {
        System.out.println("\n--- Search Items ---");
        System.out.print("Enter keyword: ");
        String keyword = scanner.nextLine().trim();
        system.searchItem(keyword);
    }

    private void claimItem() {
        System.out.println("\n--- Claim Item ---");
        System.out.print("Enter item ID to claim: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            system.claimItem(id);
        } catch (NumberFormatException e) {
            System.out.println("❌ Please enter a valid ID number!");
        }
    }
}