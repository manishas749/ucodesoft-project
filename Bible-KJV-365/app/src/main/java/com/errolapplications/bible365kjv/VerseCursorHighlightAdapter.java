package com.errolapplications.bible365kjv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class VerseCursorHighlightAdapter extends CursorAdapter {

    private String DATABASE_NAME = "bible365db111718.db";
    private int DATABASE_VERSION = 1;
    private TextView mVerseNumberTextView;
    private TextView mVerseTextView;
    private TextView mRowIdTextView;
    private Context mContext;
    private int mResource1;
    private SQLiteOpenHelper mSQLiteOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;
    public static final String TABLE_NAME = "biblekjvcombinedfull";
    public static final String VERSE_COLUMN = "field7";
    public static final String ID_COLUMN = "_id";
    private LayoutInflater mLayoutInflator;
    private boolean mNightModeSwitchState;
    private boolean mNightModeTwoSwitchState;
    private SharedPreferences mSharedPreferences;


    public VerseCursorHighlightAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.mContext = context;
        mLayoutInflator = LayoutInflater.from(context);
        //  this.mResource1 = resource1;
        // this.mVerses = verses;
        // this.mRowId = rowId;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return mLayoutInflator.inflate(R.layout.verse_list_item, viewGroup, false);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        if (convertView == null) {
            mLayoutInflator = LayoutInflater.from(mContext);
            convertView = mLayoutInflator.inflate(R.layout.verse_list_item, null);
        }

        mSharedPreferences = mContext.getSharedPreferences("FavoriteHighLightPref", 0);
        long highlightFavorites = mSharedPreferences.getLong("FavoriteHighLight", 0);

        SharedPreferences nightModeSwitchState = mContext.getSharedPreferences("SettingsActivity", 0);
        mNightModeSwitchState = nightModeSwitchState.getBoolean("NightModeSwitchState", false);

        mSharedPreferences = mContext.getSharedPreferences("FavoriteHighLightPref", 0);
        long highLightFavorites = mSharedPreferences.getLong("FavoriteHighLight", 0);

        mVerseNumberTextView = (TextView) convertView.findViewById(R.id.verseNumber);
        mVerseTextView = (TextView) convertView.findViewById(R.id.verseWords);

        if (mNightModeSwitchState || mNightModeTwoSwitchState) {

            mVerseTextView.setTextColor(mContext.getColor(R.color.card_background));
            mVerseTextView.setBackgroundColor(mContext.getColor(R.color.darker_grey));
            mVerseNumberTextView.setTextColor(mContext.getColor(R.color.card_background));
            mVerseNumberTextView.setBackgroundColor(mContext.getColor(R.color.darker_grey));

        }

        if (highlightFavorites != 0) {

        }

        convertView.setTag(position);
        return super.getView(position, convertView, arg2);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // mSQLiteOpenHelper = new SQLiteAssetHelper(mContext, DATABASE_NAME,cursor, DATABASE_VERSION);
        // mSQLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();
        mSharedPreferences = mContext.getSharedPreferences("FavoriteHighLightPref", 0);
        long highLightFavorites = mSharedPreferences.getLong("FavoriteHighLight", 0);

        int position = (Integer) view.getTag();
        position = position + 1;
        mVerseNumberTextView = (TextView) view.findViewById(R.id.verseNumber);
        mVerseNumberTextView.setText(String.valueOf(position) + " ");

        mVerseTextView = (TextView) view.findViewById(R.id.verseWords);
        @SuppressLint("Range") String verse = cursor.getString(cursor.getColumnIndex("field7"));
        //mVerseTextView.setText(verse);
        mSQLiteOpenHelper = new SQLiteAssetHelper(view.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
        mSQLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();
        cursor = mSQLiteDatabase.rawQuery("SELECT _id, field4, field5, field6, field7 FROM biblekjvcombinedfull WHERE Field8 = 1", null);
        @SuppressLint("Range") String versehighlight = cursor.getString(cursor.getColumnIndex("Field8"));

        if (versehighlight != null) {
            mSharedPreferences = view.getContext().getSharedPreferences("FavoriteChosenPrefs", Context.MODE_PRIVATE);
            mSharedPreferences.edit().putBoolean("FavoriteChosen", true).apply();
            mSharedPreferences = view.getContext().getSharedPreferences("FavoritePositionPrefs", Context.MODE_PRIVATE);
            // mSharedPreferences.edit().putInt("FavoritePosition", position).apply();
            mSharedPreferences.edit().putInt("FavoritePosition", position).apply();
            //if (rowIdSQLite != 0) {
            //mVerseTextView.setText(verse);
            Spannable verseSpan = new SpannableString(verse);
            verseSpan.setSpan(new BackgroundColorSpan(mContext.getColor(R.color.highlight_light_yellow)), 0, verse.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (mNightModeSwitchState || mNightModeTwoSwitchState) {
                verseSpan.setSpan(new BackgroundColorSpan(mContext.getColor(R.color.text_color)), 0, verse.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else
                mVerseTextView.setText(verseSpan);
            mSharedPreferences = view.getContext().getSharedPreferences("FavoriteChosenPrefs", Context.MODE_PRIVATE);
            mSharedPreferences.edit().putBoolean("FavoriteChosen", false).apply();
            //} else
            // mVerseTextView.setText(verse);
        }
        //mVerseTextView.setBackgroundColor(0xFFFFFF00);
        // Spannable spanText = Spannable.Factory.getInstance().newSpannable(verse);
        //spanText.setSpan(new BackgroundColorSpan(0xFFFFFF00), 1, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        // mVerseTextView.setText(spanText);

        // mRowIdTextView = (TextView) view.findViewById(R.id.rowId);
        //String id = cursor.getString(cursor.getColumnIndex("_id"));
        //mRowIdTextView.setText(id);


        cursor.close();

    }
}


