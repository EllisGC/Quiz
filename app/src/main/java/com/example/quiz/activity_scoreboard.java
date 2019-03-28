package com.example.quiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class activity_scoreboard extends AppCompatActivity {

    private ListView scoreListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        scoreListView = (ListView)findViewById(R.id.scoreScoreboard);

        ArrayList<Score> s = DatabaseController.getScores();

        ArrayAdapter<Score> arrayAdapter = new ArrayAdapter<Score>(this, android.R.layout.simple_list_item_1, DatabaseController.getScores());

        scoreListView.setAdapter(arrayAdapter);
    }
}
