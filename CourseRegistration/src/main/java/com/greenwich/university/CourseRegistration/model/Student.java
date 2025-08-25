package model;

import datastructures.MyLinkedList;

public class Student {
    private String id;
    private String name;
    private MyLinkedList<String> registeredCourses;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
        this.registeredCourses = new MyLinkedList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }

    public MyLinkedList<String> getRegisteredCourses() {
        return registeredCourses;
    }

    public void registerCourse(String code) {
        registeredCourses.add(code);
    }

    public void dropCourse(String code) {
        registeredCourses.remove(code);
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}
