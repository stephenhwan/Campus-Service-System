package com.greenwich.university.ui;

public class Main {
    public static void main(String[] args) {
        try {
            PrintJobManager manager = new PrintJobManager();
            manager.run();
        } catch (Exception e) {
            System.err.println("have a problem while running app");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
