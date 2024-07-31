package com.example.triviaquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class RegisterationActivity extends AppCompatActivity {

    // Arraylist to save user data
    public static ArrayList<SaveUserData> SAVEDATA = new ArrayList<SaveUserData>();
    public static String PREFERENCES = "com.trivia.preferences";
    public static String USERDATA = "user";
    SharedPreference sharedPreference;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);

        setContentView(R.layout.activity_registeration);

        Button signUpButton = findViewById(R.id.button_signUp);
        EditText firstName = findViewById(R.id.firstName);
        EditText lastName = findViewById(R.id.lastName);
        EditText email = findViewById(R.id.emailid);
        EditText password = findViewById(R.id.password);
        EditText userName = findViewById(R.id.userName);
        EditText dateOfBirth = findViewById(R.id.dateOfBirth);
        sharedPreference = new SharedPreference(this);
        db=openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE,
                null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(firstName VARCHAR,lastName VARCHAR,userName VARCHAR,dateOfBirth VARCHAR,email  VARCHAR,password VARCHAR);");

        /**
         *         validations on sign up button
         *         then we are registering the user
         *         saving it to login
         *
         */

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                firstName.setError(null);
                lastName.setError(null);
                email.setError(null);
                password.setError(null);
                userName.setError(null);
                dateOfBirth.setError(null);
                if (firstName.getText().toString().isEmpty()) {
                    firstName.setError("First name should not be empty");
                } else if (firstName.getText().toString().length() < 3 || firstName.getText().toString().length() > 30) {
                    firstName.setError("First Name Should not be less than 3 or more than 30 characters");
                } else if (lastName.getText().toString().isEmpty()) {
                    lastName.setError("Last name should not be empty");
                } else if (lastName.getText().toString().length() < 3 || lastName.getText().toString().length() > 30) {
                    lastName.setError("Last Name Should not be less than 3 or more than 30 characters");
                } else if (userName.getText().toString().isEmpty()) {
                    userName.setError("User Name Should not be empty");
                } else if (userName.getText().toString().length() < 3 || userName.getText().toString().length() > 30) {
                    userName.setError("User Name Should not be less than 3 or more than 30 characters");
                } else if (dateOfBirth.getText().toString().isEmpty()) {
                    dateOfBirth.setError("Date Of Birth should not be empty");
                } else if (dateOfBirth.getText().toString().length() < 3 || dateOfBirth.getText().toString().length() > 30) {
                    dateOfBirth.setError("Date Of Birth Should not be less than 3 or more than 30 characters");
                } else if (email.getText().toString().isEmpty()) {
                    email.setError("Email id should not be empty");
                } else if (email.getText().toString().length() < 3 || email.getText().toString().length() > 30) {
                    email.setError("email id Should not be less than 3 or more than 30 characters");
                } else if (password.getText().toString().isEmpty()) {
                    password.setError("Password should not be empty");
                } else if (password.getText().toString().length() < 3 || password.getText().toString().length() > 30) {
                    password.setError("password Should not be less than 3 or more than 30 characters");
                } else {
                    firstName.setError(null);
                    lastName.setError(null);
                    email.setError(null);
                    password.setError(null);
                    dateOfBirth.setError(null);
                    userName.setError(null);

                    // saved user data to shared preference
//                    SAVEDATA.add(new SaveUserData(firstName.getText().toString(), lastName.getText().toString(),
//                            userName.getText().toString(), dateOfBirth.getText().toString(), email.getText().toString(), password.getText().toString()));
//                    Log.d("kajkdjkjd", "onClick: "+SAVEDATA);
           //         sharedPreference.saveUserData(SAVEDATA);
                    db.execSQL("INSERT INTO student VALUES('"+firstName.getText()+"','"+lastName.getText()+
                            "','"+userName.getText()+"','"+dateOfBirth.getText()+"','"+email.getText()+"','"+password.getText()+"');");

                    Toast.makeText(
                            RegisterationActivity.this, getResources().getString(R.string.AccountCreated), Toast.LENGTH_SHORT
                    ).show();
                    Intent intent = new Intent(RegisterationActivity.this, MainActivity.class);
                    startActivity(intent);
                    RegisterationActivity.this.finish();
                }
            }
        });


    }
}