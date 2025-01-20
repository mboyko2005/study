package com.example.study;

public class Course {
    private final String title;
    private int progress; // Прогресс курса (в процентах)
    private final int imageResId;

    public Course(String title, int imageResId) {
        this.title = title;
        this.imageResId = imageResId;
        this.progress = 0; // Прогресс по умолчанию 0%
    }

    public String getTitle() {
        return title;
    }

    public int getProgress() {
        return progress;
    }

    public void updateProgress(int newProgress) {
        this.progress = newProgress;
    }

    public int getImageResId() {
        return imageResId;
    }
}
