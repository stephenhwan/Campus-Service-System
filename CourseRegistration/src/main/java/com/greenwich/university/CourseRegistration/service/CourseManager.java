package service;

import datastructures.MyArray;
import datastructures.MyComparator;
import datastructures.MyHeap;
import model.Course;

public class CourseManager {
    private MyArray<Course> catalog;

    public CourseManager() {
        catalog = new MyArray<>();
    }

    public void addCourse(Course course) {
        catalog.add(course);
    }

    public void removeCourse(int index) {
        catalog.removeAt(index);
    }

    public MyArray<Course> getCatalog() {
        return catalog;
    }

    public Course findByCode(String code) {
        for (int i = 0; i < catalog.size(); i++) {
            if (catalog.get(i).getCode().equalsIgnoreCase(code)) {
                return catalog.get(i);
            }
        }
        return null;
    }

    public Course findByName(String name) {
        for (int i = 0; i < catalog.size(); i++) {
            if (catalog.get(i).getName().equalsIgnoreCase(name)) {
                return catalog.get(i);
            }
        }
        return null;
    }

    // --- Option 3: Display all courses in a clean table
    public void printCourseCatalog() {
        System.out.println("\n📚 Course Catalog:");
        System.out.println("---------------------------------------------------------------");
        System.out.printf("%-10s %-25s %-10s %-10s %-10s%n",
                "Code", "Name", "Enrolled", "Capacity", "Waitlist");
        System.out.println("---------------------------------------------------------------");

        for (int i = 0; i < catalog.size(); i++) {
            Course c = catalog.get(i);
            System.out.printf("%-10s %-25s %-10d %-10d %-10d%n",
                    c.getCode(), c.getName(), c.getEnrolled(),
                    c.getCapacity(), c.getWaitlist().size());
        }
        System.out.println("---------------------------------------------------------------");
    }

    // --- Option 4: Show courses ranked by popularity
    public void printPopularCourses() {
        MyHeap<Course> heap = new MyHeap<>(new MyComparator<>() {
            @Override
            public int compare(Course a, Course b) {
                return Integer.compare(a.getEnrolled(), b.getEnrolled());
            }
        });
        for (int i = 0; i < catalog.size(); i++) {
            heap.insert(catalog.get(i));
        }

        System.out.println("\n🔥 Popular Courses (Ranked by Enrollment):");
        System.out.println("---------------------------------------------------------------");
        System.out.printf("%-5s %-10s %-25s %-10s%n",
                "Rank", "Code", "Name", "Enrolled");
        System.out.println("---------------------------------------------------------------");

        int rank = 1;
        while (!heap.isEmpty()) {
            Course c = heap.extractMax();
            System.out.printf("%-5d %-10s %-25s %-10d%n",
                    rank++, c.getCode(), c.getName(), c.getEnrolled());
        }
        System.out.println("---------------------------------------------------------------");
    }
}
