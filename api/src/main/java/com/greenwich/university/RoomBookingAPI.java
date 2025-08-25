package com.greenwich.university;

import com.greenwich.university.RoomBookingSystem.mainRoomBookingSystem;

public class RoomBookingAPI {
    public void start() {
        mainRoomBookingSystem roomBooking = new mainRoomBookingSystem();
        roomBooking.run();
    }
}