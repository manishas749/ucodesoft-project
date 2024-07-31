package com.errolapplications.bible365kjv;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.appcompat.app.AppCompatActivity;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;


public class TodaysVerseActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_preferences";
    public static final String TODAYS_VERSE_STRING = "todays_verse_string";
    public static final String TODAYS_BOOK_NAME = "todays_book_name_string";
    public static final String TODAYS_CHAPTER_NUMBER = "todays_chapter_number_string";
    public static final String TODAYS_VERSE_ID_COLUMN_NAME = "field1";
    public static final String TODAYS_VERSE_VERSE_COLUMN_NAME = "field7";
    public static final String TODAYS_VERSE_BOOK_MAIN_COLUMN_NAME = "field4";
    public static final String TODAYS_VERSE_CHAPTER_NUM_COLUMN_NAME = "field5";
    public static final String TODAYS_VERSE_VERSE_NUM_COLUMN_NAME = "field6";
    public static final String TODAYS_VERSE_FAVORITES_FLAG_COLUMN = "Field8";
    public static final String BIBLE_365_CHANNEL_ID = "todays_verse_notifications";
    public static final String TODAYS_VERSE_NUMBER = "com.errolapplications.bible365.todays_verse_number";
    private static final String TAG = "ListDataActivity";
    private static final String DATABASE_NAME = "bible365db111718.db";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteDatabase mSQLiteDatabase;
    private String SHARED_PREFS_FILE = "com.errolapplications.bible365.saveprefs";
    private String TODAYS_BOOK = "com.errolaplications.bible365.todays_book";
    private String TODAYS_CHAPTER = "com.errolapplications.bible365.todays_chapter";
    private String TODAYS_VERSE = "com.errolapplications.bible365.todays_verse";

    private String mVersionName = BuildConfig.VERSION_NAME;

    public void DailyVerseDatabaseAccess() {

        SQLiteOpenHelper sqLiteOpenHelper = new SQLiteAssetHelper(this, DATABASE_NAME, null, DATABASE_VERSION);

        SQLiteDatabase SqlDb = sqLiteOpenHelper.getReadableDatabase();

        String rawQuery = "SELECT * FROM kjvbibledailyverse ORDER BY RANDOM() LIMIT 1";

        Cursor cursor = SqlDb.rawQuery(rawQuery, null);
        DailyVerseCursorAdapter DVCursorAdapter = new DailyVerseCursorAdapter(this, cursor);

    }

}
