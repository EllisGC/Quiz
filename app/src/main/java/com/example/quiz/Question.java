package com.example.quiz;


import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class Question {

    private String Question;
    private String Imagepath;
    private ArrayList<String> Answers;
    private int SelectedAnswer;
    private int CorrectAnswer;


    public Question(String question, String imagepath, ArrayList<String> answers, int correctAnswer) {
        Question = question;
        Imagepath = imagepath;
        Answers = answers;
        CorrectAnswer = correctAnswer;
    }

    public int getCorrectAnswer() {
        return CorrectAnswer;
    }

    public int getSelectedAnswer() {
        return SelectedAnswer;
    }

    public void setSelectedAnswer(int selectedAnswer) {
        SelectedAnswer = selectedAnswer;
    }

    public String getQuestion() {
        return Question;
    }


    public String getImagepath() {
        return Imagepath;
    }


    public ArrayList<String> getAnswers() {
        return Answers;
    }


}

