package com.errolapplications.bible365kjv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class LastFavoriteVerseCursorAdapter extends CursorAdapter {

    private boolean mNightModeSwitchState;
    private SharedPreferences mSharedPreferences;
    private String mGetBook;
    private String mGetChapter;
    private String mVerseNumber;
    private String mVerse;
    private String mDailyVerseItem;

    public LastFavoriteVerseCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);

        int row2 = c.getPosition();

        Log.d("row adapter bind = ", String.valueOf(row2));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int row3 = cursor.getPosition();

        Log.d("row adapter newView = ", String.valueOf(row3));
        return LayoutInflater.from(context).inflate(R.layout.last_favorite_verse_item, parent, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView bookTextView = view.findViewById(R.id.fav_book_name);
        TextView chapterTextView = view.findViewById(R.id.fav_chapter);
        TextView numberTextView = view.findViewById(R.id.fav_number);
        TextView verseTextView = view.findViewById(R.id.fav_verse);

        String book = cursor.getString(cursor.getColumnIndexOrThrow("field4"));
        String chapter = cursor.getString(cursor.getColumnIndexOrThrow("field5"));
        String number = cursor.getString(cursor.getColumnIndexOrThrow("field6"));
        String verse = cursor.getString(cursor.getColumnIndexOrThrow("field7"));

        mSharedPreferences = view.getContext().getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = mSharedPreferences.getBoolean("NightModeSwitchState", false);

        mSharedPreferences = view.getContext().getSharedPreferences("RowPositionPrefs", 0);
        long rowIdSQLite = mSharedPreferences.getLong("RowPositionForFavorite", 0);

        if (LastFavoriteVerseCursorAdapter.this.isEmpty()) {

            mSharedPreferences = view.getContext().getSharedPreferences("RowPositionPrefs", 0);
            mSharedPreferences.edit().putLong("RowPositionForFavorite", 0).apply();

            verseTextView.setText("No favorites yet.");
            bookTextView.setText("");
            chapterTextView.setText("");
            numberTextView.setText("");
        }

        if (mNightModeSwitchState) {
            bookTextView.setTextColor(context.getColor(R.color.card_background));
            bookTextView.setBackgroundColor(context.getColor(R.color.dark_grey));
            chapterTextView.setTextColor(context.getColor(R.color.card_background));
            chapterTextView.setBackgroundColor(context.getColor(R.color.dark_grey));
            numberTextView.setTextColor(context.getColor(R.color.card_background));
            numberTextView.setBackgroundColor(context.getColor(R.color.dark_grey));
            verseTextView.setTextColor(context.getColor(R.color.card_background));
            verseTextView.setBackgroundColor(context.getColor(R.color.dark_grey));
        }

      /* if (cursor.getCount() == 0) {
       // if (rowIdSQLite == 0) {
            mSharedPreferences = view.getContext().getSharedPreferences("RowPositionPrefs", 0);
            mSharedPreferences.edit().putLong("RowPositionForFavorite", 0).apply();
            verseTextView.setText("No favorites selected.");
            bookTextView.setText("");
            chapterTextView.setText("");
            numberTextView.setText("");
        }

       */

        bookTextView.setText(book + " ");
        chapterTextView.setText(chapter + ":");
        numberTextView.setText(number);
        verseTextView.setText(verse);
        int row = cursor.getPosition();

        mGetBook = bookTextView.getText().toString().replace(" ", "");
        mGetChapter = chapterTextView.getText().toString().replace(":", "");
        mVerseNumber = numberTextView.getText().toString();
        mVerse = verseTextView.getText().toString();
        mDailyVerseItem = mGetBook + mGetChapter + ":" + mVerseNumber + "\n\n" + mVerse;


        mSharedPreferences = context.getSharedPreferences("favorite_verse_prefs", 0);
        mSharedPreferences.edit().putString("fav_book", mGetBook).apply();
        mSharedPreferences.edit().putString("fav_chapter", mGetChapter).apply();
        mSharedPreferences.edit().putString("fav_verse_number", mVerseNumber).apply();
        mSharedPreferences.edit().putString("fav_verse", mVerse).apply();
        mSharedPreferences.edit().putString("favorite_verse_item", mDailyVerseItem).apply();


        Log.d("row adapter bindView = ", String.valueOf(row));

    }


}
