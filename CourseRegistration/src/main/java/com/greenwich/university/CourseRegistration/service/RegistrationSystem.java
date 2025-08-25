package service;

import datastructures.MyHashTable;
import model.Course;
import model.Student;

public class RegistrationSystem {
    private MyHashTable<String, Course> courses;
    private MyHashTable<String, Student> students;

    public RegistrationSystem() {
        courses = new MyHashTable<>();
        students = new MyHashTable<>();
    }

    public void addCourse(Course c) {
        courses.put(c.getCode(), c);
    }

    public void addStudent(Student s) {
        students.put(s.getId(), s);
    }

    public boolean register(String studentId, String courseCode) {
        Student s = students.get(studentId);
        Course c = courses.get(courseCode);
        if (s == null || c == null) return false;

        if (c.enroll(studentId)) {
            s.registerCourse(courseCode);
            System.out.println("✅ " + s.getName() + " registered for " + c.getName());
            return true;
        } else {
            System.out.println("⚠️ " + c.getName() + " is full. Added " + s.getName() + " to waitlist.");
            return false;
        }
    }

    public void drop(String studentId, String courseCode) {
        Student s = students.get(studentId);
        Course c = courses.get(courseCode);

        if (s != null && c != null) {
            s.dropCourse(courseCode);
            String promotedId = c.drop(studentId);

            System.out.println("❌ " + s.getName() + " dropped " + c.getName());

            if (promotedId != null) {
                Student promoted = students.get(promotedId);
                promoted.registerCourse(courseCode);
                System.out.println("✅ " + promoted.getName() + " registered for " + c.getName() + " (promoted from waitlist).");
            }
        }
    }

    public void printStudentCourses(String studentId) {
        Student s = students.get(studentId);
        if (s != null) {
            System.out.println("📘 Courses for " + s.getName() + ":");
            for (String code : s.getRegisteredCourses()) {
                System.out.println("   - " + code);
            }
        }
    }
}
