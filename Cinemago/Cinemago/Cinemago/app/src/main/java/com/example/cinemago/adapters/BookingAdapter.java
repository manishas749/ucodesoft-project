package com.example.cinemago.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemago.R;
import com.example.cinemago.models.Booking;
import com.example.cinemago.models.Cinema;
import com.example.cinemago.models.Movie;
import com.example.cinemago.models.Review;
import com.example.cinemago.models.Showtime;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    Context context;
    private List<Booking> dataList;
    private LayoutInflater inflater;

    // Constructor
    public BookingAdapter(Context context, List<Booking> dataList) {
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking data = dataList.get(position);
        holder.ticket.setText("Ticket # "+String.valueOf(position+1));
        holder.status.setText("Status: "+data.getStatus());
        holder.total.setText("Total: $"+String.valueOf(data.getTotalPrice()));
        String seats = "";
        for (int i : data.getSeats()) {
            seats = seats + String.valueOf(i) + "  ";
        }

        holder.seats.setText("Seats: "+seats);

        fetchBookingData(data.getCinemaId(), data.getMovieId(), holder.cinemaname, holder.cinemaaddress, holder.moviename);
    }

    public void fetchBookingData(String cinemaid, String movieid, TextView cinemaname, TextView cinemaaddress, TextView moviename) {


        DatabaseReference cenimaReference = FirebaseDatabase.getInstance().getReference("cinemas");

        cenimaReference.child(cinemaid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                String name = snapshot.child("name").getValue(String.class);
                String address = snapshot.child("address").getValue(String.class);
                Log.e("CinemaDataAdapter", cinemaid);
                Log.e("CinemaDataAdapter", name);
                Log.e("CinemaDataAdapter", address);
                cinemaname.setText("Cinema Name: "+name);
                cinemaaddress.setText("Cinema Address: "+address);

                cenimaReference.child(cinemaid).child("movies").child(movieid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {

                        String name = snapshot1.child("name").getValue(String.class);
                        moviename.setText("Movie Name: "+name);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Database error: " + databaseError.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ticket, cinemaname, cinemaaddress, moviename, seats, status, total;

        ViewHolder(View itemView) {
            super(itemView);
            ticket = itemView.findViewById(R.id.ticket);
            status = itemView.findViewById(R.id.status);
            total = itemView.findViewById(R.id.total);
            cinemaname = itemView.findViewById(R.id.cinemaname);
            cinemaaddress = itemView.findViewById(R.id.cinemaaddress);
            moviename = itemView.findViewById(R.id.moviename);
            seats = itemView.findViewById(R.id.seats);
        }
    }

}
