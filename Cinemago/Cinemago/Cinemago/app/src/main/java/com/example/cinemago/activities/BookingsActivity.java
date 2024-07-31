package com.example.cinemago.activities;

import static com.example.cinemago.SharedPreference.userData;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemago.BookNowInterface;
import com.example.cinemago.R;
import com.example.cinemago.SharedPreference;
import com.example.cinemago.adapters.BookingAdapter;
import com.example.cinemago.adapters.CinemasAdapter;
import com.example.cinemago.adapters.MoviesAdapter;
import com.example.cinemago.models.Booking;
import com.example.cinemago.models.Movie;
import com.example.cinemago.utils.SingletonClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the booking activity and here we are showing all the movies booking done by the user
 */

public class BookingsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;   // intialized recyclerview
    private List<Booking> dataList = new ArrayList<>();  //Array list of booking type
    private BookingAdapter adapter;
    private SharedPreference sharedPreference;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);
        sharedPreference = new SharedPreference(this);
        sharedPreference.loadUserData();


        /**
         * took a instance from firebase of booking table in which booking is saved
         */

        databaseReference = FirebaseDatabase.getInstance().getReference("bookings");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BookingAdapter(this, dataList);
        recyclerView.setAdapter(adapter);
        fetchBookings(); //booking fetched from booking table and saved it in a dataList and later on passed to adapter to set in recyclerview

    }


    public void fetchBookings() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Booking booking = snapshot.getValue(Booking.class);
                    if (booking != null && booking.getUserId().equals(userData.uid)) {
                        dataList.add(booking);
                    }
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Database error: " + databaseError.getMessage());
            }
        });
    }

}