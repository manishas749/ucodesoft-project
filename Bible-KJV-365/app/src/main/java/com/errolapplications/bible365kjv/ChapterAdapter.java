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

class ChapterAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private int mResource1;
    private List<String> mChapters;
    private boolean mNightModeSwitchState;
    private TextView mChapterNumberTextView;

    public ChapterAdapter(Context context, int resource1, List<String> chapters) {
        super(context, resource1, chapters);
        mContext = context;
        mResource1 = resource1;
        mChapters = chapters;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        SharedPreferences nightModeSwitchState = getContext().getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = nightModeSwitchState.getBoolean("NightModeSwitchState", false);

        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.chapter_list_item, parent, false);
        }

        mChapterNumberTextView = (TextView) listItem.findViewById(R.id.chapterNumber);
        mChapterNumberTextView.setText(mChapters.get(position));

        if (mNightModeSwitchState) {

            mChapterNumberTextView.setTextColor(getContext().getColor(R.color.card_background));
            mChapterNumberTextView.setBackgroundColor(getContext().getColor(R.color.darker_grey));

        }

        return listItem;
    }
}