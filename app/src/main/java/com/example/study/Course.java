package com.example.study;

import androidx.annotation.NonNull;

public class Course {
    private final String title;
    private int progress; // Прогресс курса (в процентах)
    private final int imageResId;

    public Course(@NonNull String title, int imageResId) {
        this.title = title;
        this.imageResId = imageResId;
        this.progress = 0; // Прогресс по умолчанию 0%
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public int getProgress() {
        return progress;
    }

    public void updateProgress(int newProgress) {
        this.progress = Math.max(0, Math.min(100, newProgress)); // Ограничение от 0 до 100
    }

    public int getImageResId() {
        return imageResId;
    }
}
