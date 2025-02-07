package com.example.study.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "course_progress")
public class CourseProgress {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String courseTitle;
    private int progress;

    public CourseProgress(int userId, String courseTitle, int progress) {
        this.userId = userId;
        this.courseTitle = courseTitle;
        this.progress = progress;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
