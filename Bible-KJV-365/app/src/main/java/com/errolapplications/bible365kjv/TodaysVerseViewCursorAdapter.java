package com.errolapplications.bible365kjv;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class TodaysVerseViewCursorAdapter extends CursorAdapter {

    private boolean mNightModeSwitchState;
    private SharedPreferences mSharedPreferences;
    private String mGetBook;
    private String mGetChapter;
    private String mVerseNumber;
    private String mVerse;
    private String mDailyVerseItem;

    public TodaysVerseViewCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);

        int row2 = c.getPosition();

        Log.d("row adapter bind = ", String.valueOf(row2));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        int row3 = cursor.getPosition();

        Log.d("row adapter newView = ", String.valueOf(row3));
        return LayoutInflater.from(context).inflate(R.layout.todays_verse_view_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView bookTextView =    view.findViewById(R.id.bookName);
        TextView chapterTextView =  view.findViewById(R.id.chapter);
        TextView numberTextView =  view.findViewById(R.id.number);
        TextView verseTextView =  view.findViewById(R.id.verse);

        String book = cursor.getString(cursor.getColumnIndexOrThrow("book_main"));
        String chapter = cursor.getString(cursor.getColumnIndexOrThrow("chapter_number"));
        String number = cursor.getString(cursor.getColumnIndexOrThrow("verse_number"));
        String verse = cursor.getString(cursor.getColumnIndexOrThrow("verse"));

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


        mSharedPreferences = context.getSharedPreferences("todays_verse_prefs", 0);
        mSharedPreferences.edit().putString("book", mGetBook).apply();
        mSharedPreferences.edit().putString("chapter", mGetChapter).apply();
        mSharedPreferences.edit().putString("verse_number", mVerseNumber).apply();
        mSharedPreferences.edit().putString("verse", mVerse).apply();
        mSharedPreferences.edit().putString("daily_verse_item", mDailyVerseItem).apply();


        Log.d("row adapter bindView = ", String.valueOf(row));

    }


}
