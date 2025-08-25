package com.greenwich.university.CourseRegistration;

import model.Course;
import model.Student;
import service.CourseManager;
import service.RegistrationSystem;
import util.InputValidator;

import java.util.Scanner;

public class registrationMain {
    public void run() {
        Scanner scanner = new Scanner(System.in);
        CourseManager courseManager = new CourseManager();
        RegistrationSystem registrationSystem = new RegistrationSystem();

        System.out.println("===================================");
        System.out.println(" Welcome to Course Registration ");
        System.out.println("===================================");

        while (true) {
            System.out.println("\nSelect your role:");
            System.out.println("1. Admin");
            System.out.println("2. Student");
            System.out.println("3. Exit");
            System.out.print("Choice: ");
            String roleChoice = scanner.nextLine();

            if (roleChoice.equals("1")) {
                // ---------------- ADMIN MENU ----------------
                while (true) {
                    System.out.println("\n--- Admin Menu ---");
                    System.out.println("1. Add course");
                    System.out.println("2. Remove course");
                    System.out.println("3. List courses");
                    System.out.println("4. Show popular courses");
                    System.out.println("5. Back to main menu");
                    System.out.print("Choice: ");
                    String adminChoice = scanner.nextLine();

                    if (adminChoice.equals("1")) {
                        System.out.print("Enter course code: ");
                        String code = scanner.nextLine();
                        if (!InputValidator.validateCourseCode(code)) {
                            System.out.println(" Invalid course code! Example format: CS101");
                            continue;
                        }

                        System.out.print("Enter course name: ");
                        String name = scanner.nextLine();
                        if (!InputValidator.validateName(name)) {
                            System.out.println(" Invalid name! Must be at least 2 characters.");
                            continue;
                        }

                        System.out.print("Enter course capacity: ");
                        int capacity;
                        try {
                            capacity = Integer.parseInt(scanner.nextLine());
                        } catch (NumberFormatException e) {
                            System.out.println(" Invalid capacity! Must be a number.");
                            continue;
                        }

                        Course course = new Course(code, name, capacity);
                        courseManager.addCourse(course);
                        registrationSystem.addCourse(course);
                        System.out.println(" Course added: " + course);

                    } else if (adminChoice.equals("2")) {
                        System.out.println("Available courses:");
                        for (int i = 0; i < courseManager.getCatalog().size(); i++) {
                            System.out.println(i + ": " + courseManager.getCatalog().get(i));
                        }
                        System.out.print("Enter index to remove: ");
                        try {
                            int idx = Integer.parseInt(scanner.nextLine());
                            courseManager.removeCourse(idx);
                            System.out.println(" Course removed.");
                        } catch (Exception e) {
                            System.out.println(" Invalid index!");
                        }

                    } else if (adminChoice.equals("3")) {
                        courseManager.printCourseCatalog();

                    } else if (adminChoice.equals("4")) {
                        courseManager.printPopularCourses();

                    } else if (adminChoice.equals("5")) {
                        break; // back to main menu
                    } else {
                        System.out.println(" Invalid choice.");
                    }
                }

            } else if (roleChoice.equals("2")) {
                // ---------------- STUDENT MENU ----------------
                System.out.print("Enter student ID: ");
                String studentId = scanner.nextLine();
                if (!InputValidator.validateId(studentId)) {
                    System.out.println(" Invalid student ID! Must be at least 4 digits.");
                    continue;
                }

                System.out.print("Enter student name: ");
                String studentName = scanner.nextLine();
                if (!InputValidator.validateName(studentName)) {
                    System.out.println(" Invalid name! Must be at least 2 characters.");
                    continue;
                }

                Student student = new Student(studentId, studentName);
                registrationSystem.addStudent(student);

                while (true) {
                    System.out.println("\n--- Student Menu ---");
                    System.out.println("1. Register for a course");
                    System.out.println("2. Drop a course");
                    System.out.println("3. View my courses");
                    System.out.println("4. Back to main menu");
                    System.out.print("Choice: ");
                    String studentChoice = scanner.nextLine();

                    if (studentChoice.equals("1")) {
                        System.out.print("Enter course code: ");
                        String courseCode = scanner.nextLine();
                        if (!InputValidator.validateCourseCode(courseCode)) {
                            System.out.println(" Invalid course code! Example: CS101");
                            continue;
                        }
                        registrationSystem.register(studentId, courseCode);

                    } else if (studentChoice.equals("2")) {
                        System.out.print("Enter course code to drop: ");
                        String courseCode = scanner.nextLine();
                        if (!InputValidator.validateCourseCode(courseCode)) {
                            System.out.println(" Invalid course code! Example: CS101");
                            continue;
                        }
                        registrationSystem.drop(studentId, courseCode);

                    } else if (studentChoice.equals("3")) {
                        registrationSystem.printStudentCourses(studentId);

                    } else if (studentChoice.equals("4")) {
                        break; // back to main menu
                    } else {
                        System.out.println(" Invalid choice.");
                    }
                }

            } else if (roleChoice.equals("3")) {
                System.out.println(" Exiting system. Goodbye!");
                break;
            } else {
                System.out.println(" Invalid choice. Try again.");
            }
        }
    }
}
