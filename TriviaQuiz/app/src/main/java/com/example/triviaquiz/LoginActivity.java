package com.example.triviaquiz;

import static android.icu.text.ListFormatter.Type.AND;
import static com.example.triviaquiz.RegisterationActivity.SAVEDATA;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    SharedPreference sharedPreference;

    // static variable to save logged in user
    public static String LOGGEDINUSER = "";
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);

        setContentView(R.layout.activity_login);
        sharedPreference = new SharedPreference(this);
        sharedPreference.loadUserData();
        Button signInButton = findViewById(R.id.button_signIn);
        EditText userName = findViewById(R.id.userName);
        EditText password = findViewById(R.id.Password);


        /**
         *  sign in with the registered user with validations
         *  first we are checking the value should not empty
         *  after checking validation we are logging in with the registered user
         */

        db=openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE,
                null);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                userName.setError(null);
                password.setError(null);
                if (userName.getText().toString().isEmpty()) {
                    userName.setError("userName  should not be empty");
                } else if (userName.getText().toString().length() < 3 || userName.getText().toString().length() > 30) {
                    userName.setError("email id Should not be less than 3 or more than 30 characters");
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("Password should not be empty");
                } else if (password.getText().toString().length() < 3 || password.getText().toString().length() > 30) {
                    password.setError("password Should not be less than 3 or more than 30 characters");
                } else {
                    userName.setError(null);
                    password.setError(null);

                    Cursor c=db.rawQuery("SELECT * FROM student WHERE userName='"+userName.getText()+ "' AND password='"+password.getText()+"'", null);
                    
                    sharedPreference.saveToken(userName.getText().toString());

                    if(c.moveToFirst()) {
                        Toast.makeText(
                                v.getContext(), getResources().getString(R.string.LoggedIn), Toast.LENGTH_SHORT
                        ).show();
                        Intent intent = new Intent(LoginActivity.this, QuizHistoryActivity.class);
                        startActivity(intent);
                        finish();


                    }
                    else
                    {
                        Toast.makeText(
                                v.getContext(), getResources().getString(R.string.Incorrect), Toast.LENGTH_SHORT
                        ).show();

                    }

//                    int count = 0;
//                    for (int i = 0; i < SAVEDATA.size(); i++) {
//                        if (userName.getText().toString().equals(SAVEDATA.get(i).userName) && password.getText().toString()
//                                .equals(SAVEDATA.get(i).password)) {
//                            //saved user in shared preference







                }
            }
        });

    }
}