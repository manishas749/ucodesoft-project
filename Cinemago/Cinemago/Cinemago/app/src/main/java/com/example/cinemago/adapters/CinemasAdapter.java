package com.example.cinemago.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemago.activities.MoviesActivity;
import com.example.cinemago.R;
import com.example.cinemago.models.Cinema;
import com.example.cinemago.models.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CinemasAdapter extends RecyclerView.Adapter<CinemasAdapter.ViewHolder> {
    Context context;
    private List<Cinema> dataList;
    private LayoutInflater inflater;

    // Constructor
    public CinemasAdapter(Context context, List<Cinema> dataList) {
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_cinema, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cinema data = dataList.get(position);
        holder.tvtitle.setText(data.getName());
        holder.tvdesription.setText(data.getAddress());
        fetchReviewsAndCalculateAverage(data.getId(), holder.ratingBar, holder.ratingtext);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, MoviesActivity.class).putExtra("data", (Serializable) data));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public double fetchReviewsAndCalculateAverage(String cinemaid, RatingBar ratingBar, TextView ratingtext) {
        final double[] averageRating = {0.0};
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cinemas").child(cinemaid).child("reviews");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Review> reviews = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Review review = snapshot.getValue(Review.class);
                    reviews.add(review);
                }
                averageRating[0] = calculateAverageRating(reviews);
                System.out.println("Average Rating: " + averageRating[0]);
                ratingtext.setText(String.valueOf(averageRating[0]));
                ratingBar.setRating(Float.parseFloat(String.valueOf(averageRating[0])));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Database error: " + databaseError.getMessage());
            }
        });

        return averageRating[0];

    }

    private double calculateAverageRating(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return 0;
        }

        int sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return (double) sum / reviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvtitle;
        TextView tvdesription, ratingtext;
        ImageView imageView;
        RatingBar ratingBar;

        ViewHolder(View itemView) {
            super(itemView);
            tvtitle = itemView.findViewById(R.id.tvtitle);
            tvdesription = itemView.findViewById(R.id.tvdescription);
            imageView = itemView.findViewById(R.id.image);
            ratingtext = itemView.findViewById(R.id.ratingtext);
            ratingBar = itemView.findViewById(R.id.rating);
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
