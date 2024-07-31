package com.example.triviaquiz;

import static com.example.triviaquiz.LoginActivity.LOGGEDINUSER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     *     Arraylist to save user name and score
     */
    public static ArrayList<SaveUserGameScore> SAVEUSERSCOREHISTORY = new ArrayList<SaveUserGameScore>();
    public static String USERSCORE = "userScore";
    SharedPreference sharedPreference;


    int score = 0;
    int totalQuestions = QuestionAnswer.questions.length;
    int currentQuestionIndex = 0;
    String selectedAnswer = "";
    Button ansA;
    Button ansB;
    Button ansC;
    Button ansD;
    Button next;
    Boolean clicked;
    TextView question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);

        setContentView(R.layout.activity_quiz);
        sharedPreference = new SharedPreference(this);

        question = findViewById(R.id.question);
        TextView totalQuestionsTextview = findViewById(R.id.totalQuestion);
        ansA = findViewById(R.id.button_ansA);
        ansB = findViewById(R.id.button_ansB);
        ansC = findViewById(R.id.button_ansC);
        ansD = findViewById(R.id.button_ansD);
        next = findViewById(R.id.button_next);
        totalQuestionsTextview.setText("Total questions : " + totalQuestions);
        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        next.setOnClickListener(this);
        loadNewQuestions();


    }

    // to load next questions
    private void loadNewQuestions() {
        if (currentQuestionIndex == totalQuestions) {
            finishQuiz();
            return;
        }
        question.setText(QuestionAnswer.questions[currentQuestionIndex]);
        ansA.setText(QuestionAnswer.choices[currentQuestionIndex][0]);
        ansB.setText(QuestionAnswer.choices[currentQuestionIndex][1]);
        ansC.setText(QuestionAnswer.choices[currentQuestionIndex][2]);
        ansD.setText(QuestionAnswer.choices[currentQuestionIndex][3]);


    }



    private void finishQuiz() {
        SAVEUSERSCOREHISTORY.add(new SaveUserGameScore(sharedPreference.getToken(), score));
        sharedPreference.saveUserScoreData(SAVEUSERSCOREHISTORY);
        new AlertDialog.Builder(this).setTitle("RESULT")
                .setMessage("Score is " + score + " out of " + totalQuestions).setPositiveButton("End Game"
                        , ((dialogInterface, i) -> endGame())).setCancelable(false).show();
    }

    /**
     *     once game will end score will be set to 0 and go to quizhistory activity
      */
    private void endGame() {
        score = 0;
        currentQuestionIndex = 0;
        Intent intent = new Intent(QuizActivity.this, QuizHistoryActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onClick(View view) {
        ansA.setBackgroundColor(Color.RED);
        ansB.setBackgroundColor(Color.RED);
        ansC.setBackgroundColor(Color.RED);
        ansD.setBackgroundColor(Color.RED);

        Button clickButton = (Button) view;
        if (clickButton.getId() == R.id.button_next) {
            if (selectedAnswer!="") {
                new AlertDialog.Builder(this).setTitle("Are you sure?")
                        .setMessage("Your selected answer is " + "" +selectedAnswer +". "+ "Are you sure you want to continue?").setPositiveButton("Yes"
                                , ((dialogInterface, i) -> lockAnswer())).setCancelable(false).
                        setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        selectedAnswer = "";


                                        dialog.cancel();
                                    }
                                }).show();
            }
            else
            {
                Toast.makeText(
                        this, getResources().getString(R.string.selectCorrect), Toast.LENGTH_SHORT
                ).show();
            }


        } else {
            selectedAnswer = clickButton.getText().toString();
            clickButton.setBackgroundColor(Color.BLUE);
        }

    }

    /**
     *     once user click on yes it will save answer  and score will increase and next question will load
      */
    private void lockAnswer() {
        if (selectedAnswer.equals(QuestionAnswer.correctAnswers[currentQuestionIndex])) {
            score++;
        }
        currentQuestionIndex++;
        loadNewQuestions();
        selectedAnswer = "";

    }
}