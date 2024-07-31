package com.example.cinemago.adapters;

import static com.example.cinemago.Constants.POSITION;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemago.BookNowInterface;
import com.example.cinemago.R;
import com.example.cinemago.activities.MovieDetailsActivity;
import com.example.cinemago.models.Movie;
import com.example.cinemago.models.Showtime;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {
    Context context;
    private List<Movie> dataList;
    private LayoutInflater inflater;

    private BookNowInterface bookNowInterface;
    String cinemaid;

    // Constructor
    public MoviesAdapter(Context context, List<Movie> dataList, String cinemaid,BookNowInterface bookNowInterface) {
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
        this.context = context;
        this.cinemaid = cinemaid;
        this.bookNowInterface = bookNowInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie data = dataList.get(position);
        holder.tvtitle.setText(data.getName());
//        holder.tvdesription.setText(data.getDescription());
     //   Picasso.get().load(data.getImage()).into(holder.imageView);

        holder.bookbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                POSITION = holder.getAdapterPosition();
                bookNowInterface.bookNow(cinemaid);


            }
        });
        fetchData(holder.recyclerView, position);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    private void fetchData(RecyclerView recyclerView, int position) {

        Log.e("TimesCheck", cinemaid);

        ArrayList<Showtime> showtimeArrayList = new ArrayList<>();
//        showtimeArrayList.clear();

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cinemas").child(cinemaid).child("movies").child(dataList.get(position).getId()).child("showtimes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                showtimeArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String id = snapshot.getKey();
                    Showtime data = snapshot.getValue(Showtime.class);
                    Log.e("TimesCheck", String.valueOf(showtimeArrayList.size()));
                    showtimeArrayList.add(data);
                }
                dataList.get(position).setShowtimes(showtimeArrayList);
                recyclerView.setAdapter(new TimesAdapter(context, showtimeArrayList));
//                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Failed to load notifications.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvtitle;
        TextView tvdesription;
        ImageView imageView;
        RecyclerView recyclerView;
        Button bookbutton;

        ViewHolder(View itemView) {
            super(itemView);
            tvtitle = itemView.findViewById(R.id.tvtitle);
            imageView = itemView.findViewById(R.id.image);
            bookbutton = itemView.findViewById(R.id.book);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }

}
