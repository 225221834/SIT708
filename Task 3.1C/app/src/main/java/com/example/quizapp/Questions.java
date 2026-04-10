package com.example.quizapp;

public class Questions {
    String text;
    String[] options;
    int correctIndex;

    public Questions(String text, String[] options, int correctIndex) {
        this.text = text;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public String getText() { return text; }
    public String[] getOptions() { return options; }
    public int getCorrectIndex() { return correctIndex; }
}