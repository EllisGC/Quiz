package com.example.quiz;

import android.support.annotation.NonNull;

public class Score {
    private String Name;
    private int Score;

    public Score(){

    }

    public Score(String name, int score) {
        Name = name;
        Score = score;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }

    @NonNull
    @Override
    public String toString() {
        return "Name: " + getName() + "      Score: " + getScore();
    }
}
