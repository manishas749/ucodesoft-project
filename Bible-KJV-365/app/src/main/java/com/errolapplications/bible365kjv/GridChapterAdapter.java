package com.errolapplications.bible365kjv;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

class GridChapterAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int mResource1;
    private int mResource2;
    private List<String> mChapterNumber;

    private TextView mChapterNumberTextView;
    private boolean mNightModeSwitchState;


    public GridChapterAdapter(Context context, int resource1, List<String> chapterNumber) {
        super(context, resource1, chapterNumber);
        mContext = context;
        mResource1 = resource1;
        mChapterNumber = chapterNumber;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        SharedPreferences nightModeSwitchState = getContext().getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = nightModeSwitchState.getBoolean("NightModeSwitchState", false);

        View listItem = convertView;
        int pos = position + 1;

        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(mResource1, parent, false);
        }
        mChapterNumberTextView = listItem.findViewById(R.id.chapterNumber);

        mChapterNumberTextView.setText(mChapterNumber.get(position));

        if (mNightModeSwitchState) {

            mChapterNumberTextView.setTextColor(getContext().getColor(R.color.card_background));
            mChapterNumberTextView.setBackgroundColor(getContext().getColor(R.color.darker_grey));

        }

        return listItem;
    }
}
