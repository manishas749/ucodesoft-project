package com.example.cinemago.activities;

import static com.example.cinemago.SharedPreference.userData;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cinemago.R;
import com.example.cinemago.models.Cinema;
import com.example.cinemago.models.Review;
import com.example.cinemago.utils.SingletonClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * This activity is to save reviews under cinemas table against cinemaid
 */
public class AddReviewActivity extends AppCompatActivity {

    RatingBar ratingBar;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);

        ratingBar = findViewById(R.id.rating);
        editText = findViewById(R.id.comment);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals(""))
                {
                    Toast.makeText(AddReviewActivity.this, "Please enter a comment!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    saveData();
                }
            }
        });

    }

    /**
     * function to save data under reviews against cinemaid
     */

    private void saveData() {

        Review review = new Review();
        review.setRating((int) ratingBar.getRating());
        review.setComment(editText.getText().toString());
        review.setUserId(userData.uid);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Review...");
        progressDialog.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cinemas")
                .child(getIntent().getStringExtra("cinemaid")).child("reviews");
        String id = reference.push().getKey();
        reference.child(id).setValue(review).addOnCompleteListener(task1 -> {
            progressDialog.dismiss();
            if (task1.isSuccessful()) {
                finish();
            } else {
                Toast.makeText(AddReviewActivity.this, "Failed to add. Try again", Toast.LENGTH_LONG).show();
            }
        });
    }

}