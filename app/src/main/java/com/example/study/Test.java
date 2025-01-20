package com.example.study;

public class Test {
    private final String question;
    private final String[] options;
    private final int correctAnswerIndex;
    private boolean answeredCorrectly;

    public Test(String question, String[] options, int correctAnswerIndex) {
        this.question = question;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
        this.answeredCorrectly = false; // По умолчанию ответ неверен
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public boolean isAnsweredCorrectly() {
        return answeredCorrectly;
    }

    public void setAnsweredCorrectly(boolean answeredCorrectly) {
        this.answeredCorrectly = answeredCorrectly;
    }
}
