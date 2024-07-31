package com.example.cinemago.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemago.R;
import com.example.cinemago.models.Movie;
import com.example.cinemago.models.Showtime;

import java.util.List;

public class TimesVerticalAdapter extends RecyclerView.Adapter<TimesVerticalAdapter.ViewHolder> {
    Context context;
    private List<Showtime> dataList;
    private LayoutInflater inflater;
    Movie movie;

    // Constructor
    public TimesVerticalAdapter(Context context, List<Showtime> dataList, Movie movie) {
        this.inflater = LayoutInflater.from(context);
        this.dataList = dataList;
        this.context = context;
        this.movie = movie;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_time_vertical, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Showtime data = dataList.get(position);
        holder.day.setText(data.getDay());
        holder.time.setText(data.getTime());
        holder.name.setText(movie.getName());
        holder.language.setText("Language: "+movie.getLanguage());
        holder.duration.setText("Duration: "+movie.getDuration());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView day, time, name, duration, language;

        ViewHolder(View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            language = itemView.findViewById(R.id.language);
            duration = itemView.findViewById(R.id.duration);
        }
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
