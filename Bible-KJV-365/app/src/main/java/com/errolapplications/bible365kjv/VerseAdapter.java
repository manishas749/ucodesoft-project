package com.errolapplications.bible365kjv;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class VerseAdapter extends ArrayAdapter<String> {
    //public class VerseAdapter extends ArrayAdapter {

    private Context mContext;
    private int mResource1;
    private int mResource2;
    public List<String> mVerses;
    private List<String> mRowId;
    private TextView mVerseTextView;
    private TextView mVerseNumberTextView;
    private TextView mRowIdTextView;
    private boolean mNightModeSwitchState;
    private boolean mNightModeTwoSwitchState;

    public VerseAdapter(Activity context, int resource1, List<String> verses, List<String> rowId) {
        // public VerseAdapter(Context context, int resource1, List<String> verses, List<String> rowId) {
        super(context, resource1, verses);
        this.mContext = context;
        this.mResource1 = resource1;
        this.mVerses = verses;
        this.mRowId = rowId;

    }

    @Override
    public int getCount() {
        return mVerses.size();
    }

    // @Override
    // public Object getItem(int i) {
    //     return (String)mVerses.get(i);
    //}

    @Override
    public long getItemId(int position) {
        return (position);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        int pos = position + 1;
        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(mResource1, parent, false);
        }

        //VerseCursorAdapter verseCursorAdapter

        //mRowId.put(position,String.valueOf(listItem.getID()));
        //mVerses.put(position,String.valueOf(mVerses.get(position).getID()));

        //mRowIdTextView = (TextView) listItem.findViewById(R.id.rowId);
//        mRowIdTextView.setText(String.valueOf(getItemId(position)));
        //mRowIdTextView.setText(String.valueOf(mRowId.get(position)));

        mVerseNumberTextView = listItem.findViewById(R.id.verseNumber);
        mVerseNumberTextView.setText((pos) + " ");

        mVerseTextView = listItem.findViewById(R.id.verseWords);
        mVerseTextView.setText(mVerses.get(position));

        // mRowIdTextView = (TextView) listItem.findViewById(R.id.rowId);
        // mRowIdTextView.setText((CharSequence) mRowId);

        SharedPreferences nightModeSwitchState = mContext.getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = nightModeSwitchState.getBoolean("NightModeSwitchState", false);

        SharedPreferences rowPosition = mContext.getSharedPreferences("RowPosition", 0);
        rowPosition.edit().putInt("RowPositionSqlite", position).apply();

        if (mNightModeSwitchState || mNightModeTwoSwitchState) {

            mVerseTextView.setTextColor(mContext.getColor(R.color.card_background));
            mVerseTextView.setBackgroundColor(mContext.getColor(R.color.darker_grey));
            mVerseNumberTextView.setTextColor(mContext.getColor(R.color.card_background));
            mVerseNumberTextView.setBackgroundColor(mContext.getColor(R.color.darker_grey));

        }

        return listItem;
    }

}