package com.rohindh.trivia;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rohindh.trivia.data.QuestionBank;
import com.rohindh.trivia.data.answerListAsynResponse;

import com.rohindh.trivia.module.Question;

import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LAST_SCORE = "SCORES";
    private ImageButton nextBtn;
    private ImageButton previousBtn;
    private Button trueBtn;
    private Button falseBtn;
    private Button resetBtn;

    private TextView counterTxt;
    private TextView questionTxt;
    private TextView scoretxt;

    int currentQuestionIndex = 0;
    int userscore;




    private ArrayList<Question> questionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //updating the previous scores and question index
        SharedPreferences getdata = getSharedPreferences(LAST_SCORE,MODE_PRIVATE);
        currentQuestionIndex = getdata.getInt("QuestionIndex",0);
        userscore = getdata.getInt("score",0);


        nextBtn = findViewById(R.id.NextBtn);
        previousBtn = findViewById(R.id.PreviousBtn);
        trueBtn = findViewById(R.id.TrueBtn);
        falseBtn = findViewById(R.id.FalseBtn);
        resetBtn =findViewById(R.id.resetBtn);

        scoretxt  = findViewById(R.id.scoreTxt);
        questionTxt = findViewById(R.id.QuestionText);
        counterTxt = findViewById(R.id.countor_text);


        nextBtn.setOnClickListener(this);
        previousBtn.setOnClickListener(this);
        trueBtn.setOnClickListener(this);
        falseBtn.setOnClickListener(this);
        resetBtn.setOnClickListener(this);

        //get the arraylist from answerLisAsynRespones
        new QuestionBank().getQuestions(new answerListAsynResponse() {
            @Override
            public void ProcessFinished(ArrayList<Question> questionArrayList){
                questionTxt.setText(questionArrayList.get(currentQuestionIndex).getQuestion());
                questionList = questionArrayList;
                String counter = (currentQuestionIndex+1) + " out of "+questionList.size();
                scoretxt.setText("Scores : "+userscore);
//                highScoreTxt.setText("High score : " + highScore);
                counterTxt.setText(counter);

            }
        });
    }
    //on click listeners for buttons
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.NextBtn:
                currentQuestionIndex = (currentQuestionIndex +1) % questionList.size();
                updateQuestion();
                break;
            case R.id.PreviousBtn:
                currentQuestionIndex = (currentQuestionIndex -1) % questionList.size();
                updateQuestion();
                break;
            case R.id.TrueBtn:
                checkAnser(true);
                updateQuestion();
                break;
            case R.id.FalseBtn:
                checkAnser(false);
                updateQuestion();
                break;
            case R.id.resetBtn:
                reset();
        }
    }

    private void reset() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to reset your game ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                currentQuestionIndex = 0;
                userscore = 0;
                updateQuestion();
            }
        }).setNegativeButton("No",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    //checking the answers,animation call,score and update the view
    private void checkAnser(boolean userAnswer ) {
        if (userAnswer == questionList.get(currentQuestionIndex).getQuestionTrue()){
            Toast.makeText(this,"correct answer",Toast.LENGTH_SHORT).show();
            score(1);
            crtAnsFade();
            currentQuestionIndex = (currentQuestionIndex+1) % questionList.size();
            updateQuestion();
        }else{
            Toast.makeText(this,"wrong answer",Toast.LENGTH_SHORT).show();
            currentQuestionIndex = (currentQuestionIndex+1) % questionList.size();
            score(0);
            wrongAnsShake();
        }

    }

    //Score updated
    private void score(int ans){
        if (ans == 1){
            userscore += 100;
        }else {
            userscore -= 50;
        }
        scoretxt.setText("Scores : "+userscore);
    }

    //update the question view and question index
    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getQuestion();
        String counter = (currentQuestionIndex+1)+" out of "+questionList.size();
        scoretxt.setText("Scores : "+userscore);
        counterTxt.setText(counter);
        questionTxt.setText(question);

    }

    //correct answer animation
    private  void crtAnsFade(){
        final CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    //wrong answer animation
    private void wrongAnsShake(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shakeanim);
        final CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    //on back button clicked

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to close the game ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.super.onBackPressed();
             }
        }).setNegativeButton("No",null).setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    //saving the data for future use by share preferences
    //share Preferences on onstop because onstop is the end of the activity cycle

    @Override
    protected void onStop() {

        SharedPreferences sharedPreferences = getSharedPreferences(LAST_SCORE,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("QuestionIndex",currentQuestionIndex);
        editor.putInt("score",userscore);
        editor.apply();
        super.onStop();
    }



}
