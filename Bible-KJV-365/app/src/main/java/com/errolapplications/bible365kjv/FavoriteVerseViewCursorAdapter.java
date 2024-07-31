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

public class FavoriteVerseViewCursorAdapter extends CursorAdapter {

    private boolean mNightModeSwitchState;
    private SharedPreferences mSharedPreferences;
    private String mGetBook;
    private String mGetChapter;
    private String mVerseNumber;
    private String mVerse;
    private String mDailyVerseItem;


    public FavoriteVerseViewCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);

        int row2 = c.getPosition();

        Log.d("row adapter bind = ", String.valueOf(row2));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int row3 = cursor.getPosition();

        Log.d("row adapter newView = ", String.valueOf(row3));
        return LayoutInflater.from(context).inflate(R.layout.favorite_verse_view_item, parent, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView bookTextView =    view.findViewById(R.id.fav_book_name);
        TextView chapterTextView = view.findViewById(R.id.fav_chapter);
        TextView numberTextView =  view.findViewById(R.id.fav_number);
        TextView verseTextView =   view.findViewById(R.id.fav_verse);

        String book = cursor.getString(cursor.getColumnIndexOrThrow("field4"));
        String chapter = cursor.getString(cursor.getColumnIndexOrThrow("field5"));
        String number = cursor.getString(cursor.getColumnIndexOrThrow("field6"));
        String verse = cursor.getString(cursor.getColumnIndexOrThrow("field7"));


        Log.d("row adapter bind book = ", book);

        mSharedPreferences = view.getContext().getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = mSharedPreferences.getBoolean("NightModeSwitchState", false);

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


        Log.d("row adapter bindView = ", String.valueOf(row));
        Log.d("row adapter bindView = book", book);

    }


}