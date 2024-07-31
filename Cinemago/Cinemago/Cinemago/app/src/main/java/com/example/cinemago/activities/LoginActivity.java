package com.example.cinemago.activities;

import static com.example.cinemago.SharedPreference.userData;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.cinemago.R;
import com.example.cinemago.SharedPreference;
import com.example.cinemago.models.User;
import com.example.cinemago.utils.SingletonClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This is the login activity the launcher activity
 * Here we are checking email and password from user table which is created in register activity
 * if getting matched we are logged in
 *
 */
public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textViewRegister;
    private FirebaseAuth mAuth;
    SplashScreen splashScreen;
    private SharedPreference sharedPreference;  // Created object of shared preference class


    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreference = new SharedPreference(this);  //intialized shared preference


        /**
         * Saving string type token in shared preference on click of login button
         * if it exists then we are directly opening main activity
         * else opening login activity
         */

        if (sharedPreference.getToken()!=null)
        {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class).putExtra("type", "login");
            startActivity(intent);
            finish();
        }

        mAuth = FirebaseAuth.getInstance();  // created instance of firebase

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegister);

        buttonLogin.setOnClickListener(v -> {
            loginUser(); //login user
        });

        textViewRegister.setOnClickListener(v -> {
            if (SingletonClass.getInstance().getUser() != null) {
                SingletonClass.getInstance().setUser(null);
            }
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();

        });
    }


    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Email and password must not be empty", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sign in...");
        progressDialog.show();
        /**
         * passed email password in firebase auth
         * if success the fetchUserData
         */

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Login success, now fetch user data from 'users' node
                        fetchUserData();
                    } else {
                        // If login fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
    }

    /**
     * Fetching user Data from users table from firebase and logged in
     * then going to main activity
     */
    private void fetchUserData() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference userRef = database.child(mAuth.getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    SingletonClass.getInstance().setUser(user);
                    userData = user;
                    sharedPreference.saveUserData(userData);
                    sharedPreference.saveToken("LoggedIn");
                    Toast.makeText(LoginActivity.this, "Welcome " + user.name, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class).putExtra("type", "login");
                    startActivity(intent);
                    finish();
                }

                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Failed to read user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}