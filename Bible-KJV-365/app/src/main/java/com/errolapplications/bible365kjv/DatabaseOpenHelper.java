package com.errolapplications.bible365kjv;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.Objects;

public class DatabaseOpenHelper extends SQLiteAssetHelper {
    public static final String BOOK_SECOND = "BOOK_SECOND";
    public static final String TABLE_VERSE = "bibleverse";
    public static final String TABLE_BOOKS = "biblebook";
    public static final String TABLLE_CHAPTERS = "biblebookchapters";

    public static final String TABLLE_FAVORITES = "favorites_table";
    public static final String FAVORITES_COLUMN_ID = "id";
    public static final String FAVORITES_COLUMN_BOOK = "book";
    public static final String FAVORITES_COLUMN_CHAPTER = "chapter_number";
    public static final String FAVORITES_COLUMN_VERSE_NUMBER = "verse_number";
    public static final String FAVORITES_COLUMN_VERSE = "verse";

    public static final String BIBLE_ROW_ID_COLUMN = "field1";
    public static final String BIBLE_BOOK_NAME_COLUMN = "field4";
    public static final String BIBLE_CHAPTER_NUMBER_COLUMN = "field5";
    public static final String BIBLE_VERSE_NUMBER_COLUMN = "field6";
    public static final String BIBLE_VERSE_COLUMN = "field7";
    public static final String BIBLE_CHAPTER_COLUMN_NAME = "field2";
    private static final String MAIN_TABLE = "biblekjvcombinedfull";
    private static final String BIBLE_FAVORITE_BOOLEAN_COLUMN = "Field8";
    private static final String DATABASE_NAME = "bible365db111718.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "DatabaseOpenHelper";
    private SharedPreferences mSharedPreferences;
    private int mRowPositionInt;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mSharedPreferences = Objects.requireNonNull(context.getSharedPreferences("RowPosition", 0));
        mRowPositionInt = mSharedPreferences.getInt("RowPositionSqlite", 0);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public boolean updateData(int favorite) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BIBLE_FAVORITE_BOOLEAN_COLUMN, favorite);
        String whereArgs = String.valueOf(mRowPositionInt);

        Log.d(TAG, "addData: Adding " + favorite + "to " + BIBLE_FAVORITE_BOOLEAN_COLUMN);

        db.update(MAIN_TABLE, contentValues, BIBLE_FAVORITE_BOOLEAN_COLUMN, new String[]{whereArgs});

        return false;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT BIBLE_BOOK_NAME_COLUMN, BIBLE_CHAPTER_NUMBER_COLUMN, BIBLE_VERSE_NUMBER_COLUMN, BIBLE_VERSE_COLUMN FROM MAIN_TABLE WHERE BIBLE_FAVORITE_COLUMN_NAME = 1";
        Cursor data = db.rawQuery(query, null);
        return data;


    }

}

