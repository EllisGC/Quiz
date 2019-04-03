package com.example.quiz;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class QuestionsActivity extends AppCompatActivity {

    private ArrayList<Question> questions;
    private int selectedIndex = 0;
    private boolean onLastQuestion = false;
    private DatabaseController db;
    private int score = 0;
    private String name = "";
    private boolean finished = false;
    private FirebaseAuth firebaseAuth;

    Button btn;
    ProgressBar progressBar;
    TextView questionId;
    TextView questionNumId;
    RadioGroup radioGroup;
    RadioButton questionAns4;
    RadioButton questionAns3;
    RadioButton questionAns2;
    RadioButton questionAns1;
    ImageView imageView;
    Context imageViewContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        questions = new ArrayList<>();
        btn = findViewById(R.id.button_Next);
        progressBar = findViewById(R.id.progressBar);
        db = new DatabaseController();

        questionId = (TextView)findViewById(R.id.textView_Question);
        questionNumId = (TextView)findViewById(R.id.textView_QuestionNum);
        radioGroup = (RadioGroup)findViewById(R.id.radioButtonGroup);
        questionAns1 = (RadioButton)findViewById(R.id.radioButton_Answer1);
        questionAns2 = (RadioButton)findViewById(R.id.radioButton_Answer2);
        questionAns3 = (RadioButton)findViewById(R.id.radioButton_Answer3);
        questionAns4 = (RadioButton)findViewById(R.id.radioButton_Answer4);
        imageView = (ImageView)findViewById(R.id.imageView);
        imageViewContext = imageView.getContext();



        readQuestionData();
        UpdateQuestion();
    }

    private void UpdateQuestion(){
        questionId.setText(questions.get(selectedIndex).getQuestion());

        questionNumId.setText("Question " + (selectedIndex + 1)+" of " + questions.size());

        questionAns1.setText(questions.get(selectedIndex).getAnswers().get(0));
        questionAns2.setText(questions.get(selectedIndex).getAnswers().get(1));
        questionAns3.setText(questions.get(selectedIndex).getAnswers().get(2));
        questionAns4.setText(questions.get(selectedIndex).getAnswers().get(3));
        Resources r = imageViewContext.getResources();
        int id = r.getIdentifier(questions.get(selectedIndex).getImagepath(), "drawable", imageViewContext.getPackageName());

        imageView.setImageResource(id);

        switch (questions.get(selectedIndex).getSelectedAnswer()){
            case 0:
                radioGroup.clearCheck();
                break;
            case 1:
                questionAns1.setChecked(true);
                break;
            case 2:
                questionAns2.setChecked(true);
                break;
            case 3:
                questionAns3.setChecked(true);
                break;
            case 4:
                questionAns4.setChecked(true);
        }
    }

    private void readQuestionData(){
  InputStream is = getResources().openRawResource(R.raw.questions);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                ArrayList<String> ans = new ArrayList<>();

                ans.add(data[2]);
                ans.add(data[3]);
                ans.add(data[4]);
                ans.add(data[5]);

                questions.add(new Question(data[0], data[1], ans, Integer.parseInt(data[6])));
            }
        }catch (IOException e){
                Log.d("QuestionsActivity", "Error reading data on line " + line, e);
        e.printStackTrace();
            }
    }


    public void button_Next(View view) {

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(!onLastQuestion){
            if(radioGroup.getCheckedRadioButtonId() != -1){
                if (selectedIndex != questions.size()-1){
                    int s = radioGroup.getCheckedRadioButtonId();
                    View radioButton = radioGroup.findViewById(s);
                    questions.get(selectedIndex).setSelectedAnswer(radioGroup.indexOfChild(radioButton) + 1);
                    Log.d("RADIO", Integer.toString(radioGroup.getCheckedRadioButtonId()));
                    selectedIndex++;
                    UpdateQuestion();
                    progressBar.setProgress((progressBar.getProgress() + 7));
                    if(progressBar.getProgress() > 100)
                        progressBar.setProgress(100);
                }
                if(selectedIndex == questions.size()-1)
                {
                    progressBar.setProgress(100);
                    onLastQuestion = true;
                    btn.setText("Finish");
                }
                else
                    btn.setText("Next");
            }
        }else{
            if(!finished){
                int s = radioGroup.getCheckedRadioButtonId();
                View radioButton = radioGroup.findViewById(s);
                questions.get(selectedIndex).setSelectedAnswer(radioGroup.indexOfChild(radioButton) + 1);
                for(int i=0; i < questions.size(); i++){
                    if(questions.get(i).getSelectedAnswer() == questions.get(i).getCorrectAnswer()){
                        score++;
                    }
                }
                finished = true;
            }





            //If user is logged in add name and score to database
            if(user != null){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Score: " + score);

                name = user.getDisplayName();

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            if(name.length() >= 10)
                                name = name.substring(0, 9);
                            db.WriteNewScore(new Score(name, score));
                            finish();
                            startActivity(new Intent(QuestionsActivity.this, activity_scoreboard.class));


                    }
                });

                builder.show();

            } else {

                //Show dialog so user can enter name.

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Score: " + score + "/15, Enter name");

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        name = input.getText().toString();
                        if (!TextUtils.isEmpty(name)) {
                            if (name.length() >= 10)
                                name = name.substring(0, 9);
                            db.WriteNewScore(new Score(name, score));
                            startActivity(new Intent(QuestionsActivity.this, activity_scoreboard.class));
                            finish();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        }
    }


    public void button_Previous (View view) {
        if(selectedIndex != 0)
        {
            selectedIndex--;
            UpdateQuestion();
            progressBar.setProgress((progressBar.getProgress() - 7));
            if(selectedIndex == 0)
                progressBar.setProgress(0);

        }
        btn.setText("Next");
        onLastQuestion = false;
    }



}
