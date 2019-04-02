package com.example.quiz;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseController.StartController();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        if(user != null){
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_user, menu);
            return true;
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.itemLogin:
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                return true;
            case R.id.itemProfile:
                //Start user profile
                //startActivity(new Intent(MainActivity.this, SignInActivity.class));
                return true;
            case R.id.itemLogout:
                firebaseAuth.signOut();
                finish();
                startActivity(getIntent());

                return true;
            default:return super.onOptionsItemSelected(item);
        }
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
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }




}
