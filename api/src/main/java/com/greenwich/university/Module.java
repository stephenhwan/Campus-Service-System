package com.greenwich.university;

import java.util.Scanner;

public interface Module {
    String name();           // tên hiển thị trong menu
    void run(Scanner in);    // chạy module; return để quay lại menu
}