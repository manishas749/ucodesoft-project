package com.example.triviaquiz;

import static com.example.triviaquiz.QuizActivity.SAVEUSERSCOREHISTORY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

public class QuizHistoryActivity extends AppCompatActivity {

    TextView score;
    Button logout;
    SharedPreference sharedPreference;
    public static ArrayList<SaveUserGameScore> ParticularUser = new ArrayList<SaveUserGameScore>();
    Button playNow;
    View scoreOne;
    TextView lastScore;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);

        setContentView(R.layout.activity_quiz_history);
        sharedPreference = new SharedPreference(this);
        sharedPreference.loadUserScoreData();
        logout = findViewById(R.id.logout);
        score = findViewById(R.id.score);
        playNow = findViewById(R.id.playNow);
        scoreOne = findViewById(R.id.scoreLayout);
        lastScore = findViewById(R.id.lastScore);
        Log.d("size", "onCreate: " + ParticularUser.size());
        for (int i = 0; i < SAVEUSERSCOREHISTORY.size(); i++) {
            if (Objects.equals(SAVEUSERSCOREHISTORY.get(i).UserName, sharedPreference.getToken())) {
                ParticularUser.add(SAVEUSERSCOREHISTORY.get(i));

            }
        }


        if (ParticularUser.size() > 0) {
            scoreOne.setVisibility(View.VISIBLE);
            lastScore.setVisibility(View.VISIBLE);
            Integer a = ParticularUser.get(ParticularUser.size() - 1).Score;
            score.setText(a.toString());
        }


        /**
         *         here we are logout the particular user
          */
        logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                sharedPreference.clearToken();
                Intent intent = new Intent(QuizHistoryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        /**
         *         To play game again
          */

        playNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(QuizHistoryActivity.this, QuizActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }
}