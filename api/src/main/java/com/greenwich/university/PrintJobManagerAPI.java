package com.greenwich.university;

import com.greenwich.university.ui.PrintJobManager; // từ print-job-manager module

public class PrintJobManagerAPI {
    public void start() {
        PrintJobManager manager = new PrintJobManager();
        manager.run();
    }
}