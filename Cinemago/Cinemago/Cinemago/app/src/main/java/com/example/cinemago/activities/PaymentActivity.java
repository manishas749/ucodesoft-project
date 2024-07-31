package com.example.cinemago.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;

import com.example.cinemago.R;
import com.example.cinemago.models.Booking;
import com.example.cinemago.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Payment activity to get payment from user
 * we are showing total amount here we has been passed through intent
 * and seats saved in booking table passed through intent
 */
public class PaymentActivity extends AppCompatActivity {

    private TextView tvPaymentMessage;
    private EditText editTextCardNumber, editTextExpiryDate, editTextCVC, amountet;
    private Button buttonSubmitPayment;
    private DatabaseReference databaseReference;
    double total_price = 0.0;
    String amount = "";

    private FirebaseAuth mAuth;
    List<Integer> seats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("Payment");
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        amountet = findViewById(R.id.amount);
        tvPaymentMessage = findViewById(R.id.tvPaymentMessage);
        editTextCardNumber = findViewById(R.id.editTextCardNumber);
        editTextExpiryDate = findViewById(R.id.editTextExpiryDate);
        editTextCVC = findViewById(R.id.editTextCVC);
        buttonSubmitPayment = findViewById(R.id.buttonSubmitPayment);

        seats = (List<Integer>) getIntent().getSerializableExtra("seats");
        // Get the amount from the intent
        double amount = getIntent().getDoubleExtra("total_price", 0.0);
        tvPaymentMessage.setText("Your payable zakat amount is " + amount + ". Please enter your payment details.");

        if (amount == 0.0)
        {
            tvPaymentMessage.setText("Please enter your payment details to pay");
        }
        amountet.setText(String.valueOf(amount));

        databaseReference = FirebaseDatabase.getInstance().getReference("bookings");

        buttonSubmitPayment.setOnClickListener(v -> {
            if (validatePaymentDetails()) {
                saveBookingData();
            }
        });
    }
    private boolean validatePaymentDetails() {
        if (amountet.getText().toString().isEmpty() ||editTextCardNumber.getText().toString().isEmpty() ||
                editTextExpiryDate.getText().toString().isEmpty() ||
                editTextCVC.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
            return false;
        }
        else if (amountet.getText().toString().equals("0"))
        {
            Toast.makeText(this, "Amount should be greater than 0", Toast.LENGTH_LONG).show();
            return false;
        }
        // Additional validation can be added here
        return true;
    }


    private void saveBookingData() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Booking...");
        progressDialog.show();

        String uid = mAuth.getCurrentUser().getUid();

            Booking booking = new Booking(
                    getIntent().getStringExtra("cinemaid"),
                    "abc",
                    getIntent().getStringExtra("movieid"),
                    seats,
                    "Confirmed",
                    "abc",
                    (int) getIntent().getDoubleExtra("total_price", 0.0),
                    uid
                    );

            databaseReference.push().setValue(booking).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                    {
                        startActivity(new Intent(PaymentActivity.this, BookingSuccessActivity.class));
                        finish();
                    }
                }
            });

    }

}
