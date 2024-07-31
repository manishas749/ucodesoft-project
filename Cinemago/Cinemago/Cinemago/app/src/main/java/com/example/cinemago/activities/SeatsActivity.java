package com.example.cinemago.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cinemago.R;
import com.example.cinemago.models.Booking;
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

/**
 * Seat activity to book seats once seat selected selected seats are placed in a arraylist and then saved in firebase
 *
 */
public class SeatsActivity extends AppCompatActivity {

    ImageView seat1, seat2, seat3, seat4, seat5, seat6, seat7, seat8, seat9, seat10;
    ImageView seat11, seat12, seat13, seat14, seat15, seat16, seat17, seat18, seat19, seat20;
    ImageView seat21, seat22, seat23, seat24, seat25, seat26, seat27, seat28, seat29, seat30;
    ImageView seat31, seat32, seat33, seat34, seat35, seat36, seat37, seat38, seat39, seat40;
    ImageView seat41, seat42, seat43, seat44, seat45, seat46, seat47, seat48, seat49, seat50;
    ImageView seat51, seat52;
    List<Integer> myselectedseats = new ArrayList<>();


    private DatabaseReference databaseReference;
    private List<Integer> alreadybookedseats;
    ImageView[] seats;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seats);

        movie = (Movie) getIntent().getSerializableExtra("data");
        Log.e("MOVVVV", movie.getId());

        databaseReference = FirebaseDatabase.getInstance().getReference("bookings");
        alreadybookedseats = new ArrayList<>();
        fetchBookings();
        initializeseats();
        // Array to hold all seat ImageViews
        seats = new ImageView[]{seat1, seat2, seat3, seat4, seat5, seat6, seat7, seat8, seat9, seat10,
                seat11, seat12, seat13, seat14, seat15, seat16, seat17, seat18, seat19, seat20,
                seat21, seat22, seat23, seat24, seat25, seat26, seat27, seat28, seat29, seat30,
                seat31, seat32, seat33, seat34, seat35, seat36, seat37, seat38, seat39, seat40,
                seat41, seat42, seat43, seat44, seat45, seat46, seat47, seat48, seat49, seat50,
                seat51, seat52};


        // Check each seat and set the drawable accordingly
        for (int i = 0; i < seats.length; i++) {
            final int seatNumber = i + 1;  // Seat numbers start at 1, index at 0
            ImageView seat = seats[i];

            // Set click listener for each seat
            seat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean found = false;
                    for (int seatModel : SeatsActivity.this.alreadybookedseats) {
                        if (seatModel == seatNumber) {
                            seat.setImageResource(R.drawable.seat_grey);
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                    {
                        if (myselectedseats.contains(seatNumber))
                        {
                            removeItemSafely(myselectedseats, seatNumber);  // Removes the value 2
                            seat.setImageResource(R.drawable.seat_white);
                        }
                        else
                        {
                            myselectedseats.add(seatNumber);
                            seat.setImageResource(R.drawable.seat_grey);
                        }
                    }
                    selectSeat((ImageView) v, seatNumber);
                }
            });
        }

        findViewById(R.id.checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myselectedseats.size() > 0)
                {
                    double total = getIntent().getDoubleExtra("ticketprice",0.0) * myselectedseats.size();
                    startActivity(new Intent(SeatsActivity.this, PaymentActivity.class)
                            .putExtra("cinemaid", getIntent().getStringExtra("cinemaid"))
                            .putExtra("total_price", total)
                            .putExtra("seats", (Serializable) myselectedseats)
                            .putExtra("movieid", movie.getId()));
                    finish();
                }
                else
                {
                    Toast.makeText(SeatsActivity.this, "Please select a seat first!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void selectSeat(ImageView seat, int seatNumber) {
        // You can customize this function to further handle seat selection
//        Toast.makeText(this, "Seat " + seatNumber + " selected", Toast.LENGTH_SHORT).show();
        // Example change of state, can be adjusted based on the app's logic
//        seat.setImageResource(R.drawable.seat_selected);
    }

    public static void removeItemSafely(List<Integer> list, Integer value) {
        if (list == null) {
            System.out.println("The list is null.");
            return;
        }

        // Check if the value could be a valid index
        if (value >= 0 && value < list.size()) {
            // Here you decide what to do if the value is a valid index:
            // If you want to ensure only value-based removals:
            if (list.contains(value)) {
                list.remove(value);
                System.out.println("Removed value: " + value);
            } else {
                System.out.println("Value not found, no removal performed.");
            }
        } else {
            // If it's not a valid index, we can try to remove by value
            if (list.contains(value)) {
                list.remove(value);
                System.out.println("Removed value: " + value);
            } else {
                System.out.println("Value not found or invalid index, no removal performed.");
            }
        }
    }

    private void initializeseats() {

        // Initialize each ImageView variable
        seat1 = findViewById(R.id.seat1);
        seat2 = findViewById(R.id.seat2);
        seat3 = findViewById(R.id.seat3);
        seat4 = findViewById(R.id.seat4);
        seat5 = findViewById(R.id.seat5);
        seat6 = findViewById(R.id.seat6);
        seat7 = findViewById(R.id.seat7);
        seat8 = findViewById(R.id.seat8);
        seat9 = findViewById(R.id.seat9);
        seat10 = findViewById(R.id.seat10);
        seat11 = findViewById(R.id.seat11);
        seat12 = findViewById(R.id.seat12);
        seat13 = findViewById(R.id.seat13);
        seat14 = findViewById(R.id.seat14);
        seat15 = findViewById(R.id.seat15);
        seat16 = findViewById(R.id.seat16);
        seat17 = findViewById(R.id.seat17);
        seat18 = findViewById(R.id.seat18);
        seat19 = findViewById(R.id.seat19);
        seat20 = findViewById(R.id.seat20);
        seat21 = findViewById(R.id.seat21);
        seat22 = findViewById(R.id.seat22);
        seat23 = findViewById(R.id.seat23);
        seat24 = findViewById(R.id.seat24);
        seat25 = findViewById(R.id.seat25);
        seat26 = findViewById(R.id.seat26);
        seat27 = findViewById(R.id.seat27);
        seat28 = findViewById(R.id.seat28);
        seat29 = findViewById(R.id.seat29);
        seat30 = findViewById(R.id.seat30);
        seat31 = findViewById(R.id.seat31);
        seat32 = findViewById(R.id.seat32);
        seat33 = findViewById(R.id.seat33);
        seat34 = findViewById(R.id.seat34);
        seat35 = findViewById(R.id.seat35);
        seat36 = findViewById(R.id.seat36);
        seat37 = findViewById(R.id.seat37);
        seat38 = findViewById(R.id.seat38);
        seat39 = findViewById(R.id.seat39);
        seat40 = findViewById(R.id.seat40);
        seat41 = findViewById(R.id.seat41);
        seat42 = findViewById(R.id.seat42);
        seat43 = findViewById(R.id.seat43);
        seat44 = findViewById(R.id.seat44);
        seat45 = findViewById(R.id.seat45);
        seat46 = findViewById(R.id.seat46);
        seat47 = findViewById(R.id.seat47);
        seat48 = findViewById(R.id.seat48);
        seat49 = findViewById(R.id.seat49);
        seat50 = findViewById(R.id.seat50);
        seat51 = findViewById(R.id.seat51);
        seat52 = findViewById(R.id.seat52);
    }

    public void fetchBookings() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                alreadybookedseats.clear(); // Clear previous data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Booking booking = snapshot.getValue(Booking.class);
                    if (booking != null &&
                            booking.getCinemaId().equals(getIntent().getStringExtra("cinemaid")) &&
                            booking.getMovieId().equals(movie.getId())) {
                        List<Integer> seats = booking.getSeats();
                        if (seats != null) {
                            alreadybookedseats.addAll(seats);
                        }
                    }
                }
                System.out.println("All matched seats: " + alreadybookedseats);
                // Here you can call another method or trigger an event with the collected seats

                // Check each seat and set the drawable accordingly
                for (int i = 0; i < seats.length; i++) {
                    final int seatNumber = i + 1;  // Seat numbers start at 1, index at 0
                    ImageView seat = seats[i];

                    // Default to unbooked (white)
                    seat.setImageResource(R.drawable.seat_white);

                    // Check if the seat is booked
                    for (int booking : alreadybookedseats) {
                        if (booking == seatNumber) {
                            seat.setImageResource(R.drawable.seat_grey);
                            break;
                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Database error: " + databaseError.getMessage());
            }
        });
    }

}