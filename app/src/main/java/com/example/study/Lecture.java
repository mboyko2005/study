package com.example.study;

public class Lecture {
    private final String title;
    private final String previewContent;
    private final String fullContent;

    public Lecture(String title, String previewContent, String fullContent) {
        this.title = title;
        this.previewContent = previewContent;
        this.fullContent = fullContent;
    }

    public String getTitle() {
        return title;
    }

    public String getPreviewContent() {
        return previewContent;
    }

    public String getFullContent() {
        return fullContent;
    }
}
