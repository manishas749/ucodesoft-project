package com.errolapplications.bible365kjv;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {

    private static final String LOG_TAG = WordAdapter.class.getSimpleName();

    public WordAdapter(Activity context, ArrayList<Word> words) {
        super(context, 0, words);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.reading_plan_list_item, parent, false);
        }
        Word currentWord = getItem(position);

        TextView descriptionTextView = listItemView.findViewById(R.id.description);
        assert currentWord != null;
        descriptionTextView.setText(currentWord.getDescription());

        TextView titleTextView = listItemView.findViewById(R.id.title);
        titleTextView.setText(currentWord.getTitle());

        return listItemView;
    }
}







