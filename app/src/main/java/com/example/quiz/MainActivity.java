package com.example.quiz;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.FirebaseApp;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseController.StartController();
    }


    public void button_PlayClicked(View view) {
        startActivity(new Intent(MainActivity.this, QuestionsActivity.class));
    }

    public void button_ScoreboardClicked(View view) {
        startActivity(new Intent(MainActivity.this, activity_scoreboard.class));
    }

    public void button_QuitClicked(View view) {
        System.exit(0);
    }

    public void button_PremierTable(View view) {
        Uri uri = Uri.parse("https://www.premierleague.com/tables");
        startActivity(new Intent(Intent.ACTION_VIEW, uri));

    }

    public void button_News(View view) {
        Uri uri = Uri.parse("https://www.skysports.com/premier-league");
        startActivity(new Intent(Intent.ACTION_VIEW,uri));
    }
}
