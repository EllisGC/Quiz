package com.example.quiz;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatabaseController {
    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference scoreRef = database.getReference("Scores");
    private static ArrayList<Score> scores = scores = new ArrayList<>();

    public static void StartController() {
        scoreRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                scores.clear();
                for(DataSnapshot scoreSnapshot : dataSnapshot.getChildren()){
                    Score score = scoreSnapshot.getValue(Score.class);
                    scores.add(score);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void WriteNewScore(Score score){
        String key = scoreRef.push().getKey();
        scoreRef.child(key).setValue(score);
    }

    public static ArrayList<Score> getScores() {
        return scores;
    }
}
