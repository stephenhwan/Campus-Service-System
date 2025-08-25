package com.greenwich.university;

import com.greenwich.university.CourseRegistration.registrationMain; // từ CourseRegistration module

public class CourseRegistrationAPI {
    public void start() {
        registrationMain registration = new registrationMain();
        registration.run();
    }
}