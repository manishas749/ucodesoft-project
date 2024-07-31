package com.example.triviaquiz;

import static com.example.triviaquiz.RegisterationActivity.SAVEDATA;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    SharedPreference sharedPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);

        setContentView(R.layout.activity_main);

        sharedPreference =   new SharedPreference(this);

        // if there is user in shared preference then automatically go to QuizhistoryActivity

        if (sharedPreference.getToken() != null) {
            Intent intent = new Intent(MainActivity.this, QuizHistoryActivity.class);
            startActivity(intent);
            finish();


        }

        TextView signUpButton = findViewById(R.id.btnSignUp);
        Button signInButton = findViewById(R.id.button_signIn);

        /**
         *       This button is   go to sign up Activity
         */

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterationActivity.class);
                startActivity(intent);

            }
        });

        /**
         *       This button is go to sign up Activity
         */

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);


            }
        });


    }
}