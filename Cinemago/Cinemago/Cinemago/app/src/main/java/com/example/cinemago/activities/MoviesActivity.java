package com.example.cinemago.activities;

import static com.example.cinemago.Constants.DATALIST;
import static com.example.cinemago.Constants.EMAIL;
import static com.example.cinemago.SharedPreference.userData;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemago.BookNowInterface;
import com.example.cinemago.R;
import com.example.cinemago.adapters.MoviesAdapter;
import com.example.cinemago.models.Cinema;
import com.example.cinemago.models.Movie;
import com.example.cinemago.utils.SingletonClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Activity to show all the movies listing and after clicking on particular movie book now now one
 * can book movie
 */

public class MoviesActivity extends AppCompatActivity implements BookNowInterface {

    private RecyclerView recyclerView;
    private MoviesAdapter adapter;
    private DatabaseReference databaseReference;
    TextView addresstv, contacttv, cinemaNameTextView;
    TextView listEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        cinemaNameTextView = findViewById(R.id.tvtitle);
        addresstv = findViewById(R.id.address);
        contacttv = findViewById(R.id.contact);
        listEmpty = findViewById(R.id.noMovies);

        Cinema data = (Cinema) getIntent().getSerializableExtra("data");
        cinemaNameTextView.setText(data.getName());
        addresstv.setText(data.getAddress());
        contacttv.setText(data.getContact());

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.addreview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoviesActivity.this, AddReviewActivity.class).putExtra("cinemaid", data.getId()));
            }
        });

        if (Objects.equals(userData.email, "teamdatainnovators@cinemago.com"))
        {
            findViewById(R.id.add).setVisibility(View.VISIBLE);
        }
        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoviesActivity.this, AddMovieActivity.class).putExtra("cinemaid", data.getId()));
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("cinemas").child(data.getId()).child("movies");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MoviesAdapter(this, DATALIST, data.getId(), this);
        recyclerView.setAdapter(adapter);
        fetchData();
        if (DATALIST.isEmpty())
        {
            listEmpty.setVisibility(View.VISIBLE);
        }

    }

    private void fetchData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DATALIST.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    Movie data = snapshot.getValue(Movie.class);
                    DATALIST.add(data);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MoviesActivity.this, "Failed to load notifications.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void bookNow(String cinemaId) {
        Intent intent = new Intent(MoviesActivity.this, MovieDetailsActivity.class).putExtra("cinemaId", cinemaId);
        startActivity(intent);

    }
}