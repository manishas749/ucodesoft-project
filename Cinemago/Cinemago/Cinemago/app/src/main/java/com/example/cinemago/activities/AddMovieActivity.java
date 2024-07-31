package com.example.cinemago.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cinemago.R;
import com.example.cinemago.models.Cinema;
import com.example.cinemago.models.Movie;
import com.example.cinemago.models.Showtime;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * The is a add movie class in which we are adding movies against the reference of cinemas table and under cinemaid
 */
public class AddMovieActivity extends AppCompatActivity {

    EditText name, description, duration, language, ticketprice, day, time;
    List<Showtime> showtimes = new ArrayList<>();
    String daytime = "";
    TextView daytimetextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        name = findViewById(R.id.name);
        description = findViewById(R.id.description);
        duration = findViewById(R.id.duration);
        language = findViewById(R.id.language);
        ticketprice = findViewById(R.id.ticketprice);
        day = findViewById(R.id.day);
        time = findViewById(R.id.time);
        daytimetextview = findViewById(R.id.daytimetext);

        /**
         * On this button we are adding slots for movies if fields are not empty we are saving show time in a arraylist
         * further we are saving arraying in movie table
         */
        findViewById(R.id.adddaytime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (day.getText().toString().equals("") || time.getText().toString().equals(""))
                {
                    saveData();
                }
                else
                {
                    showtimes.add(new Showtime(day.getText().toString(), time.getText().toString()));
                    daytime = daytime + "(Day: "+day.getText().toString()+", Time: "+time.getText().toString()+") ";
                    daytimetextview.setText(daytime);
                }
            }
        });

        /**
         * on add button click we are checking field not empty and then running function saveData()
         */

        findViewById(R.id.addbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")
                        || description.getText().toString().equals("")
                        || duration.getText().toString().equals("")
                        || language.getText().toString().equals("")
                        || ticketprice.getText().toString().equals("")
                )
                {
                    Toast.makeText(AddMovieActivity.this, "Please fill all the Details", Toast.LENGTH_SHORT).show();
                }
                else if (showtimes.isEmpty())
                {
                    Toast.makeText(AddMovieActivity.this, "Please enter day and time", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    saveData();  // function to save data in table under cinemaid and table name movies
                }
            }
        });

    }

    private void saveData() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving Movie Data...");
        progressDialog.show();

        Movie movie= new Movie();
        movie.setName(name.getText().toString());
        movie.setDescription(description.getText().toString());
        movie.setDuration(duration.getText().toString());
        movie.setLanguage(language.getText().toString());
        movie.setTicketprice(Integer.parseInt(ticketprice.getText().toString()));
        movie.setShowtimes(showtimes);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("cinemas").child(getIntent().getStringExtra("cinemaid")).child("movies");
        String id = reference.push().getKey();
        movie.setId(id);
        reference.child(id).setValue(movie).addOnCompleteListener(task1 -> {
            progressDialog.dismiss();
            if (task1.isSuccessful()) {
                finish();
            } else {
                Toast.makeText(AddMovieActivity.this, "Failed to add. Try again", Toast.LENGTH_LONG).show();
            }
        });
    }

}