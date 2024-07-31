package com.example.cinemago.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemago.R;
import com.example.cinemago.models.Review;
import com.example.cinemago.models.Showtime;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TimesAdapter extends RecyclerView.Adapter<TimesAdapter.ViewHolder> {
    Context context;
    private List<Showtime> dataList;
    private LayoutInflater inflater;

    // Constructor
    public TimesAdapter(Context context, List<Showtime> dataList) {
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_time, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Showtime data = dataList.get(position);
        holder.day.setText(data.getDay());
        holder.time.setText(data.getTime());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView day, time;

        ViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            time = itemView.findViewById(R.id.time);
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
