package com.example.cinemago.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemago.R;
import com.example.cinemago.models.User;
import com.example.cinemago.utils.SingletonClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * This is the registeration activity to register user to firebase in users table
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextAddress;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private TextView textViewLogin;
    private TextView title;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        editTextName = findViewById(R.id.editTextName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);
        textViewLogin = findViewById(R.id.textViewLogin);
        title = findViewById(R.id.title);

        try {
            if (SingletonClass.getInstance().getUser() != null) {
                editTextName.setText(SingletonClass.getInstance().getUser().getName());
                editTextAddress.setText(SingletonClass.getInstance().getUser().getAddress());
                editTextEmail.setText(SingletonClass.getInstance().getUser().getEmail());
                editTextEmail.setEnabled(false);
                editTextEmail.setFocusable(false);
                editTextEmail.setClickable(false);
                buttonRegister.setText("Update Profile");
                title.setText("Personal Details");
                editTextPassword.setVisibility(View.GONE);
                textViewLogin.setVisibility(View.GONE);
            }
        } catch (Exception e) {
        }

        buttonRegister.setOnClickListener(v -> {
            if (SingletonClass.getInstance().getUser() == null) {
                registerUser();
            } else {
                saveUserData();
            }
        });

        textViewLogin.setOnClickListener(v -> {
            finish();
        });
    }


    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        // Register the user in Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // User is successfully registered and logged in
                // Now store additional fields in Firebase Database
                saveUserData();
            } else {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "Could not register. Try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Function to save User data in table

    private void saveUserData() {
        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving User Data...");
        progressDialog.show();

        User user = new User(mAuth.getCurrentUser().getUid(), name, address, email);
        SingletonClass.getInstance().setUser(user);

        FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(task1 -> {
                    progressDialog.dismiss();
                    if (task1.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class).putExtra("type", "        ProgressDialog progressDialog = new ProgressDialog(this);"));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Failed to register. Try again", Toast.LENGTH_LONG).show();
                    }
                });
    }

}