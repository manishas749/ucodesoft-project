package com.errolapplications.bible365kjv;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.errolapplications.bible365kjv.model.Verse;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatabaseAccess extends AppCompatActivity {

    private static final String DATABASE_NAME = "bible365db111718.db";
    private static final int DATABASE_VERSION = 1;
    public static String mBook;
    public static String mChapter;
    public static String mNumber;
    public static String mVerse;
    public static Cursor mCursor;
    private Verse mVerses;
    public ListView mListView;
    public VerseCursorAdapter mVerseCursorAdapter;
    private static DatabaseAccess instance;
    private static DailyVerseCursorAdapter mDailyVerseCursorAdapter;
    private static final String TAG = "DatabaseAccess";
    public TextView mBookTextView;
    public TextView mChapterTextView;
    public TextView mNumberTextView;
    public TextView mVerseTextView;
    public SQLiteDatabase mSQLiteDatabase;
    public String mRawQuery;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    public static final String BIBLE_FAVORITE_COLUMN_NAME = "field8";
    public static final String TABLE_NAME = "biblekjvcombinedfull";
    public static final String VERSE_COLUMN = "field7";
    public static final String KEY_ROWID = "_id";
    private int mRowPositionInt;
    private SharedPreferences mSharedPreferences;
    private DatabaseAccess myDbAccess;
    public static final String SHARED_PREFS = "shared_preferences";
    public static final String TODAYS_VERSE_STRING = "todays_verse_string";
    public static final String TODAYS_BOOK_NAME = "todays_book_name_string";
    public static final String TODAYS_CHAPTER_NUMBER = "todays_chapter_number_string";
    public static final String TODAYS_VERSE_ID_COLUMN_NAME = "_id";
    public static final String TODAYS_VERSE_VERSE_COLUMN_NAME = "verse";
    public static final String TODAYS_VERSE_BOOK_MAIN_COLUMN_NAME = "book_main";
    public static final String TODAYS_VERSE_CHAPTER_NUM_COLUMN_NAME = "chapter_number";
    public static final String TODAYS_VERSE_VERSE_NUM_COLUMN_NAME = "verse_number";
    public static final String BIBLE_365_CHANNEL_ID = "todays_verse_notifications";
    public static final String DAILY_VERSE_INTENT_ID = "daily-verse-notification";
    public static final Boolean DEBUG_NOTIFICATIONS = true;
    private static SQLiteOpenHelper mSQLiteOpenHelper;
    private static String TODAYS_VERSE_NUMBER = "com.errolapplications.bible365.todays_verse_number";
    private String SHARED_PREFS_FILE = "com.errolapplications.bible365.saveprefs";
    private String TODAYS_BOOK = "com.errolaplications.bible365.todays_book";
    private String TODAYS_CHAPTER = "com.errolapplications.bible365.todays_chapter";
    private String TODAYS_VERSE = "com.errolapplications.bible365.todays_verse";


    public DatabaseAccess() {

    }

    public DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(FragmentActivity context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mVerseCursorAdapter = new VerseCursorAdapter(this, mCursor);

        mSQLiteOpenHelper = new SQLiteAssetHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        mSQLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();

        mBookTextView = findViewById(R.id.bookName);
        mChapterTextView = findViewById(R.id.chapter);
        mNumberTextView = findViewById(R.id.number);
        mVerseTextView = findViewById(R.id.verse);

        SharedPreferences favoritePreferences = Objects.requireNonNull(getSharedPreferences("RowPosition", 0));
        mRowPositionInt = favoritePreferences.getInt("RowPositionSqlite", 0);

        Log.d(TAG, "DatabaseAccess onCreate Called");

    }



    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    public void updateData(int favorite, CharSequence rowId) {
        open();
        // mSQLiteOpenHelper = new SQLiteAssetHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        // mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BIBLE_FAVORITE_COLUMN_NAME, favorite);
        //String[] whereArgs = new String[] {String.valueOf(rowId)};
        rowId = rowId.toString();
        //database.update(TABLE_NAME, contentValues, "_id", args);

        String whereArgs = rowId.toString();

        Log.d(TAG, "addData: Adding " + favorite + "to " + "field8");
        Log.d(TAG, "rowPosition = " + rowId);

        //SQLiteOpenHelper sqLiteOpenHelper = new SQLiteAssetHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        // mSQLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        //mRawQuery = "UPDATE biblekjvcombinedfull SET field8 = 1 WHERE field1 = whereArgs";
        mCursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull", null);
        //mCursor = mSQLiteDatabase.rawQuery(mRawQuery, null);
        // mDailyVerseCursorAdapter = new DailyVerseCursorAdapter(this, mCursor);
        database.update("biblekjvcombinedfull", contentValues, "Field8" + "= ?", new String[]{whereArgs});

    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /** public void useThisVerse() {

     Verse verseToUse = getAVerse(true);
     updateDailyVerseInActivity(verseToUse);
     }
     */

    /**
     * public Verse getAVerse(boolean usingCachedIsOk) {
     * <p>
     * SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
     * int dailyVerseRowId = sharedPreferences.getInt(TODAYS_VERSE_STRING, 0);
     * <p>
     * String rawQuery = "SELECT * FROM kjvbibledailyverse ORDER BY RANDOM() LIMIT 1";
     * String[] selectionArgs = null;
     * if (dailyVerseRowId != 0 && usingCachedIsOk) {
     * Log.d(TAG, "***********************************************************Using cached verse.");
     * rawQuery = "SELECT * FROM kjvbibledailyverse where _id == ?";
     * selectionArgs = new String[]{String.valueOf(dailyVerseRowId)};
     * }
     * <p>
     * Cursor cursor = mSQLiteDatabase.rawQuery(rawQuery, selectionArgs);
     * <p>
     * Log.d(TAG, "Rows returned " + String.valueOf(cursor.getCount()));
     * Log.d(TAG, "Column Names " + cursor.getColumnNames()[4]);
     * Log.d(TAG, "Column index for verse " + String.valueOf(cursor.getColumnIndex("verse")));
     * Log.d(TAG, "Column count " + String.valueOf(cursor.getColumnCount()));
     * <p>
     * int verseIndex = 0;
     * int bookNameIndex = 0;
     * int chapterNumberIndex = 0;
     * int verseNumberIndex = 0;
     * <p>
     * String verseString = "";
     * String bookNameString = "";
     * String chapterNumberString = "";
     * String verseNumberString = "";
     * <p>
     * int idColIndex = cursor.getColumnIndex(TODAYS_VERSE_ID_COLUMN_NAME);
     * int idxVerse = cursor.getColumnIndex(TODAYS_VERSE_VERSE_COLUMN_NAME);
     * int idxBookName = cursor.getColumnIndex(TODAYS_VERSE_BOOK_MAIN_COLUMN_NAME);
     * int idxChapterNumber = cursor.getColumnIndex(TODAYS_VERSE_CHAPTER_NUM_COLUMN_NAME);
     * int idxVerseNumber = cursor.getColumnIndex(TODAYS_VERSE_VERSE_NUM_COLUMN_NAME);
     * <p>
     * while (cursor.moveToNext()) {
     * verseIndex = cursor.getInt(idColIndex);
     * verseString = cursor.getString(idxVerse);
     * bookNameIndex = cursor.getInt(idxBookName);
     * bookNameString = cursor.getString(idxBookName);
     * chapterNumberIndex = cursor.getInt(idxChapterNumber);
     * chapterNumberString = cursor.getString(idxChapterNumber);
     * verseNumberIndex = cursor.getInt(idxVerseNumber);
     * verseNumberString = cursor.getString(idxVerseNumber);
     * <p>
     * }
     * <p>
     * SharedPreferences.Editor editor = sharedPreferences.edit();
     * editor.putInt(TODAYS_VERSE_STRING, verseIndex);
     * editor.putInt(TODAYS_BOOK_NAME, bookNameIndex);
     * editor.putInt(TODAYS_CHAPTER_NUMBER, chapterNumberIndex);
     * editor.putInt(TODAYS_VERSE_NUMBER, verseNumberIndex);
     * editor.apply();
     * <p>
     * return new Verse(cursor, bookNameString, chapterNumberString, verseNumberString, verseString);
     * }
     * <p>
     * public void updateDailyVerseInActivity(Verse verse) {
     * DailyVerseCursorAdapter dvca = new DailyVerseCursorAdapter(this, verse.getVerseCursor());
     * MainActivity.getInstance()mDailyVerseListView.setAdapter(dvca);
     * <p>
     * }
     * @return
     */


    public List<String> getBookOldTest() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getBooksNewTest() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 39", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersGenesis() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 50", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersExodus() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 50", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersLeviticus() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 90", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersNumbers() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 117", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersDeuteronomy() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 153", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersJoshua() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 187", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersJudges() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 211", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersRuth() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 4 OFFSET 232", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersOneSamuel() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 236", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersTwoSamuel() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 267", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersOneKings() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 291", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersTwoKings() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 313", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersOneChronicals() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 338", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersTwoChronicals() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 367", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersEzra() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 403", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersNehemiah() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 413", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersEsther() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 426", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersJob() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 436", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersPsalms() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 150 OFFSET 478", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersProverbs() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 628", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersEcclesiastes() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 659", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersSongOfSolomon() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 671", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersIsaiah() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 66 OFFSET 679", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersJeremiah() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 52 OFFSET 745", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersLamentations() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 797", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersEzekiel() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 48 OFFSET 802", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersDaniel() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 850", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersHosea() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 862", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersJoel() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 3 OFFSET 876", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersAmos() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 879", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersObadiah() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 1 OFFSET 888", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersJonah() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 4 OFFSET 889", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersMicah() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 893", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersNahum() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 3 OFFSET 900", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersHabakkuk() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 3 OFFSET 903", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersZephaniah() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 3 OFFSET 906", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersHaggai() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 2 OFFSET 909", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersZechariah() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 911", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersMalachi() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 4 OFFSET 925", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersMatthew() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 929", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersMark() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 957", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersLuke() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 973", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersJohn() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 997", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersActs() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 1018", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersRomans() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 1046", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersOneCorinthians() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 1062", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersTwoCorinthians() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 1078", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersGalations() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 1091", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersEphesians() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 1097", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersPhilippians() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 4 OFFSET 1103", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersColossians() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 4 OFFSET 1107", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersOneThessalonians() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 1111", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersTwoThessalonians() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 3 OFFSET 1116", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersOneTimothy() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 1119", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersTwoTimothy() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 4 OFFSET 1125", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersTitus() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 3 OFFSET 1129", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersPhilemon() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 1 OFFSET 1132", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersHebrews() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 1133", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersJames() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 1146", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersOnePeter() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 1151", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersTwoPeter() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 3 OFFSET 1156", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersOneJohn() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 1159", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersTwoJohn() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 1 OFFSET 1164", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersThreeJohn() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 1 OFFSET 1165", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersJude() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 1 OFFSET 1166", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getChaptersRevelation() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 1167", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(2));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 31", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            VerseCursorAdapter verseCursorAdapter = new VerseCursorAdapter(this, cursor);
            //list.add(cursor.getString(6));
           // long chatRow=cursor.getLong(
                 //   cursor.getColumnIndexOrThrow("rowID"));
           // cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 56", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 80", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 106", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 138", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 160", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 184", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 206", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 235", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 267", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 299", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 319", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 337", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 361", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 382", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 398", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 425", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 458", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 496", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 514", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 548", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 572", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 67 OFFSET 592", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 659", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 693", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 728", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterTwentyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 774", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterTwentyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 796", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterThirty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 831", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterThirtyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 55 OFFSET 874", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterThirtyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 929", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterThirtyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 961", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterThirtyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 981", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterThirtyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 1012", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterThirtySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 1041", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterThirtySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 1084", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterThirtyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 1120", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterThirtyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 1150", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterForty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 1173", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterFortyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 57 OFFSET 1196", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterFortyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 1253", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterFortyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 1291", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterFortyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 1325", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterFortyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 1359", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterFortySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 1387", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterFortySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 1421", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterFortyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 1452", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterFortyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 1474", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGenesisChapterFifty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 1507", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;


    }

    public List<String> getVersesExodusChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 1533", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 1555", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 1580", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 1602", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 1633", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 1656", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 1686", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 1711", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 1743", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 1778", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 1807", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 51 OFFSET 1817", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 1868", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 1890", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 1921", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 1948", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 1984", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 2000", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 2027", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 2052", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 2078", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 2114", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 2145", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 2178", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 2196", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 2236", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 2273", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterTwentyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 2294", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterTwentyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 2334", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterThirty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 2383", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterThirtyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 2421", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterThirtyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 2439", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterThirtyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 2474", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterThirtyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 2497", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterThirtyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 2532", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterThirtySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 2567", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterThirtySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 2605", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterThirtyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 2634", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterThirtyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 2665", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesExodusChapterForty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 2708", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 2746", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 2763", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 2779", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 2796", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 2831", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 2850", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 2880", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 2918", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 2954", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 2978", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;

    }

    public List<String> getVersesLeviticusChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 47 OFFSET 2998", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 3045", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 59 OFFSET 3053", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 57 OFFSET 3112", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 3169", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 3202", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 3236", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 3253", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 3282", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 3319", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 3346", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 3370", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 3403", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 3447", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 55 OFFSET 3470", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 3525", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLeviticusChapterTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 3571", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 54 OFFSET 3605", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 3659", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 51 OFFSET 3693", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 49 OFFSET 3744", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 3793", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 3824", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 89 OFFSET 3851", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 3940", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 3966", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 3989", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 4025", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 4060", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 4076", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 45 OFFSET 4109", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 41 OFFSET 4154", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 50 OFFSET 4195", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 4245", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 4258", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 4290", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 4312", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 4341", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 41 OFFSET 4376", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 4417", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 4447", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 4472", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 65 OFFSET 4490", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 4555", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterTwentyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 4578", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterTwentyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 4609", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterThirty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 4649", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterThirtyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 54 OFFSET 4665", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterThiryTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 4719", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterThirtyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 56 OFFSET 4761", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;


    }

    public List<String> getVersesNumbersChapterThirtyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 4817", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterThirtyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 4846", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNumbersChapterThirtySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 4880", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 4893", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 4939", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 4976", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 49 OFFSET 5005", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 5054", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 5087", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 5112", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 5138", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 5158", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 5187", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 5209", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 5241", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 5273", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 5291", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 5320", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 5343", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 5365", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 5385", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 5407", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 5428", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 5448", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 5471", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 5501", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 5526", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 5548", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 5567", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 5586", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterTwentyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 68 OFFSET 5612", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterTwentyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 5680", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterThirty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 5709", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterThirtyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 5729", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterThirtyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 52 OFFSET 5759", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterThirtyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 5811", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDeuteronomyChapterThirtyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 5840", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 5852", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 5870", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 5894", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 5911", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 5935", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 5950", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 5977", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 6003", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 6038", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 6065", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 6108", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 6131", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 6155", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 6188", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 63 OFFSET 6203", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 6266", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 6276", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 6294", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 51 OFFSET 6322", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 6373", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 45 OFFSET 6382", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 6427", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 6461", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoshuaChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 6477", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 6510", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 6546", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 6569", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 6600", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 6624", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 6655", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 6695", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 6720", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 57 OFFSET 6755", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 6812", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 6830", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 6870", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 6885", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 6910", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 6930", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 6950", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 6981", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 6994", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 7025", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 48 OFFSET 7055", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudgesChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 7103", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRuthChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 7128", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRuthChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 7150", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRuthChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 7173", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRuthChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 7191", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 7213", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 7241", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 7277", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 7298", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 7320", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 7332", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 7353", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 7370", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 7392", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 7419", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 7446", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 7461", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 7486", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 52 OFFSET 7509", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;


    }

    public List<String> getVersesOneSamuelChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 7561", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 7596", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 58 OFFSET 7619", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterEighhteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 7677", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 7707", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 7731", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 7773", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 7788", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 7811", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 7840", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 7862", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 7906", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 7931", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterTwentyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 7943", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterTwentyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 7968", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterThirty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 7979", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneSamuelChapterThirtyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 8010", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 8023", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 8050", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 8082", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 8121", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 8133", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 8158", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 8181", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 8210", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 8228", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 8241", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 8260", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 8287", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 8318", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 8357", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 8390", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 8427", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 8450", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 7479", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 8512", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 8555", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 8581", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 51 OFFSET 8603", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 8654", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoSamuelChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 8693", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 53 OFFSET 8718", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 8771", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 8817", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 8845", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 8879", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 8897", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 51 OFFSET 8935", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 66 OFFSET 8986", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 9052", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;


    }

    public List<String> getVersesOneKingsChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 9080", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 9109", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 9152", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 9185", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 9219", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 9250", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 9284", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 9318", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 9342", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 9388", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 9409", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 9452", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneKingsChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 53 OFFSET 9481", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 9534", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 9552", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 9577", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 9604", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 9648", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 9675", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 9708", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 9728", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 9757", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 9794", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 9830", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 9851", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 9872", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 9897", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 9926", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 9964", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 41 OFFSET 9984", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 10025", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 10062", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 10099", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 10120", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 10146", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 10166", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 10203", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoKingsChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 10223", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 54 OFFSET 10253", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 55 OFFSET 10307", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 10362", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 10386", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 10429", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 81 OFFSET 10455", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 10536", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 10576", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 10616", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 10660", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 47 OFFSET 10674", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 10721", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;


    }

    public List<String> getVersesOneChroniclesChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 10761", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 10775", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 10792", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 10821", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 10864", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 10891", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 10908", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 10927", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 10935", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 10965", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 10984", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 11016", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 11047", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 11078", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 11110", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterTwentyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 11144", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneChroniclesChapterTwentyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 11165", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 11195", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 11212", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 11230", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 11247", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 11269", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 11283", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 11325", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 11347", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 11365", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 11396", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 11415", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 11438", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 11454", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 11476", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 11491", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 11510", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 11524", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 11543", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 11577", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 11588", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 11625", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 11645", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 11657", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 11678", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 11705", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 11733", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 11756", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterTwentyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 11765", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterTwentyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 11792", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterThirty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 11828", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterThirtyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 11855", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterThirtyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 11876", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterThirtyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 11909", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;


    }

    public List<String> getVersesTwoChroniclesChapterThirtyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 11934", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterThirtyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 11967", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoChroniclesChapterThirtySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 11994", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzraChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 12017", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzraChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 70 OFFSET 12028", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzraChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 12098", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzraChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 12111", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzraChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 12135", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzraChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 12152", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzraChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 12174", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzraChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 120202", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzraChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 12238", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzraChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 12253", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNehemiahChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 12297", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNehemiahChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 12308", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNehemiahChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 12328", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNehemiahChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 12360", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNehemiahChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 12383", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNehemiahChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 12402", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNehemiahChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 73 OFFSET 12421", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNehemiahChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 12494", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNehemiahChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 12512", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNehemiahChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 12550", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNehemiahChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 12589", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNehemiahChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 47 OFFSET 12625", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNehemiahChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 12672", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEstherChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 12703", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEstherChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 12725", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEstherChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 12748", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEstherChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 12763", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEstherChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 12780", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEstherChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 12794", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEstherChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 12808", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEstherChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 12818", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEstherChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 12835", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEstherChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 3 OFFSET 12867", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 12870", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 12892", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 12905", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 12931", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 12952", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 12979", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 13009", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 13030", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 13052", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 13087", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 13109", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 13129", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 13154", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 13182", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;


    }

    public List<String> getVersesJobChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 13204", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 13239", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 13261", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 13277", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 13298", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 13327", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 13356", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 13390", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 13420", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 13437", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 13462", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 13468", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 13482", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterTwentyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 13505", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterTwentyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 13533", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterThirty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 13558", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterThirtyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 13589", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterThirtyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 13629", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterThirtyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 13651", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterThirtyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 13684", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterThirtyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 13721", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterThirtySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 13737", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterThirtySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 13770", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterThirtyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 41 OFFSET 13794", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterThirtyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 13835", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterForty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 13865", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterFortyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 13889", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJobChapterFortyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 13923", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 13940", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 13946", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 13958", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 13966", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 13974", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 13986", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 13996", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 14013", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 14022", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 14042", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 14060", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 14067", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 14075", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 14081", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 14088", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 14093", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 14104", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 50 OFFSET 14119", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 14169", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 14183", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 14192", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 14205", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;


    }

    public List<String> getVersesPsalmsChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 14236", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 14242", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 14252", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 14274", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 14286", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterTwentyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 14300", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterTwentyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 14309", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterThirty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 14320", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterThirtyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 14332", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterThirtyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 14356", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterThirtyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 14367", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterThirtyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 14389", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterThirtyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 14411", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterThirtySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 14439", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterThirtySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 14451", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterThirtyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 14491", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterThirtyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 14513", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterForty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 14526", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFortyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 14543", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFortyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 14556", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFortyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 14567", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFortyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 14572", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFortyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 14598", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFortySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 14615", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFortySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 14626", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFortyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 14635", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFortyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 14649", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFifty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 14669", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFiftyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 14692", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFiftyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 14711", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFiftyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 14720", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFiftyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 14726", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFiftyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 14733", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFiftySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 14756", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFiftySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 14769", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFiftyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 1041", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterFiftyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 14791", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSixty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 14808", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSixtyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 14820", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSixtyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 14828", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSixtyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 14840", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSixtyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 14851", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSixtyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 14861", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSixtySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 14874", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSixtySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 14894", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSixtyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 14901", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSixtyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 14936", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSeventy() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 14972", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSeventyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 14977", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSeventyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 15001", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;


    }

    public List<String> getVersesPsalmsChapterSeventyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 15021", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSeventyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 15049", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSeventyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 15072", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSeventySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 15082", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSeventySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 15094", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSeventyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 72 OFFSET 15114", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterSeventyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 15186", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterEighty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 15199", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterEightyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 15218", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterEightTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 15234", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterEightyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 15242", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterEightyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 15260", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterEightyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 15272", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterEightySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 15285", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterEightySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 15302", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterEightyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 15309", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterEightyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 52 OFFSET 15327", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterNinety() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 15379", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterNinetyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 15396", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterNinetyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 15412", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterNinetyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 15427", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterNinetyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 15432", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterNinetyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 15455", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterNinetySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 15466", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterNinetySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 15479", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterNinetyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 15491", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterNinetyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 15501", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundred() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 15509", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 15514", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 15522", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 15550", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 15572", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 45 OFFSET 15607", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 48 OFFSET 15652", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 15700", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 15743", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 15756", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 15787", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 15794", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 15804", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 15814", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 15823", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 15831", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 15849", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 2 OFFSET 15868", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 15871", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 176 OFFSET 15899", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 16075", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 16082", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 16090", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;

    }

    public List<String> getVersesPsalmsChapterOneHundredTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 4 OFFSET 16099", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    public List<String> getVersesPsalmsChapterOneHundredTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 16103", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 16111", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 16116", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 16122", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredTwentyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 16127", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredTwentyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 16133", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredThirty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 16141", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredThirtyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 3 OFFSET 16149", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredThirtyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 16152", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredThirtyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 3 OFFSET 16170", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredThirtyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 3 OFFSET 16173", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredThirtyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 16176", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredThirtySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 16197", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredThirtySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 16223", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredThirtyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 16232", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredThirtyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 16240", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredForty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 16264", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredFortyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 16277", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredFortyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 16287", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredFortyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 16294", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredFortyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 16306", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredFortyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 16321", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredFortySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 16342", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredFortySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 16352", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredFortyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 16372", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredFortyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 16386", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPsalmsChapterOneHundredFifty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 16395", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 16401", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 16434", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 16456", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 16491", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 16518", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 16541", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 16576", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 16603", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 16639", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 16657", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 16689", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 16720", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 16748", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 16773", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 16808", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 16841", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 16874", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 16902", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 16926", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 16955", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 16985", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 17016", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 17045", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 17080", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 17114", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 17142", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 17170", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterTwentyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 17197", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterTwentyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 17225", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterThirty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 17252", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesProverbsChapterThirtyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 17285", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEcclesiastesChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 17316", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEcclesiastesChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 17334", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEcclesiastesChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 17360", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEcclesiastesChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 17382", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEcclesiastesChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 1890", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEcclesiastesChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 17418", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEcclesiastesChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 17430", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEcclesiastesChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 17459", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEcclesiastesChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 17476", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEcclesiastesChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 17494", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEcclesiastesChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 17514", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEcclesiastesChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 17524", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesSongOfSolomonChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 17538", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesSongOfSolomonChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 17555", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesSongOfSolomonChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 17572", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesSongOfSolomonChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 17583", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesSongOfSolomonChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 17599", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesSongOfSolomonChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 17615", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesSongOfSolomonChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 17628", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesSongOfSolomonChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 17641", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 17655", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 17686", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 17708", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 17734", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 17740", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 17770", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 17783", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 17808", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 17830", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 17851", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 17885", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 17901", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 17907", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 17929", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 17961", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 17970", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 17984", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 17998", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 18005", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 18030", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
    public List<String> getVersesIsaiahChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 18036", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 18053", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 18078", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 18096", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 18119", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 18131", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 18152", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterTwentyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 18165", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterTwentyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 18194", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterThirty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 18218", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterThirtyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 18251", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterThirtyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 18260", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterThirtyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 18280", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterThirtyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 18304", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterThirtyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 18321", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterThirtySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 18331", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterThirtySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 18353", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterThirtyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 18391", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterThirtyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 18413", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterForty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 18421", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFortyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 18452", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFortyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 18481", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFortyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 18506", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFortyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 18534", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFortyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 18562", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFortySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 18587", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFortySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 18600", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFortyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 18615", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFortyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 18637", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFifty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 18663", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFiftyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 18674", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFiftyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 18697", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFiftyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 18712", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFiftyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 18724", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFiftyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 18741", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFiftySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 18754", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFiftySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 18766", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFiftyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 18787", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterFiftyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 18801", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterSixty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 18822", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterSixtyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 18844", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterSixtyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 18855", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterSixtyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 18867", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterSixtyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 18886", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterSixtyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 18898", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesIsaiahChapterSixtySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 18923", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 18947", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 18966", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 19003", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 19028", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 19059", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 19090", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 19120", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 19154", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 19176", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 19202", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 19227", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 19250", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 19268", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 19294", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 19316", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 19337", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 19358", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 19385", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 19409", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 19423", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 19441", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 19455", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 19485", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 19525", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 19535", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 19573", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 19597", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterTwentyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 19619", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterTwentyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 19636", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterThirty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 19668", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterThirtyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 19692", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterThirtyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 19732", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterThirtyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 19776", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterThirtyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 19802", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterThirtyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 19824", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterThirtySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 19843", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterThirtySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 19875", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterThirtyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 19896", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterThirtyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 19924", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterForty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 19942", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterFortyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 19958", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterFortyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 19976", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterFortyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 19998", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterFortyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 20011", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterFortyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 20041", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterFortySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 20046", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterFortySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 7 OFFSET 20074", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterFortyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 47 OFFSET 20081", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterFortyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 20128", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterFifty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 20167", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterFiftyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 64 OFFSET 20213", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJeremiahChapterFiftyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 20277", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLamentationsChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 20311", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLamentationsChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 20333", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLamentationsChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 66 OFFSET 20355", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }


    public List<String> getVersesLamentationsChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 20421", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLamentationsChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 20443", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 20465", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 20493", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 20503", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 20530", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 20547", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 20564", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 20578", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 20605", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 20623", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 20634", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 20656", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 20681", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 20709", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 20732", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 20755", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 63 OFFSET 20763", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 20826", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 20850", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 20882", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 49 OFFSET 20896", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 20945", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 20977", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 49 OFFSET 21008", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 21057", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 21084", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 21101", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 21122", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterTwentyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 21158", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterTwentyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 21184", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterThirty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 21205", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterThirtyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 21231", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterThirtyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 21249", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterThirtyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 21281", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterThirtyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 21314", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterThirtyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 21346", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterThirtySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 21360", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterThirtySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 21398", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterThirtyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 21426", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterThirtyNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 21449", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterForty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 49 OFFSET 21478", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterFortyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 21527", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterFortyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 21553", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterFortyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 21573", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterFortyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 21600", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterFortyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 21631", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterFortySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 21656", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEzekielChapterFortySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 21680", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;


    }

    public List<String> getVersesEzekielChapterFortyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 21703", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDanielChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 21738", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDanielChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 49 OFFSET 21759", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDanielChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 21808", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDanielChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 21838", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDanielChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 21875", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDanielChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 21906", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDanielChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 21934", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDanielChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 21962", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDanielChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 21989", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDanielChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 22016", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDanielChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 45 OFFSET 22037", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesDanielChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 22082", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHoseaChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 22095", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHoseaChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 22106", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHoseaChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 5 OFFSET 22129", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHoseaChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 22134", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHoseaChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22153", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHoseaChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 22168", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHoseaChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 22179", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHoseaChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 22195", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHoseaChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 22209", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHoseaChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22226", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHoseaChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 22241", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHoseaChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 22253", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHoseaChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 22267", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHoseaChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 22283", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoelChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 22292", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoelChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 22312", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJoelChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 22344", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesAmosChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22365", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesAmosChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 22380", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesAmosChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22396", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesAmosChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 22411", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesAmosChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 22424", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesAmosChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 22451", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesAmosChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 22465", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesAmosChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 22482", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesAmosChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22496", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesObadiahChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 22511", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJonahChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 22532", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJonahChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 22559", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJonahChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 22569", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJonahChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 22569", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMicahChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 22580", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMicahChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 22596", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMicahChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 22609", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMicahChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 22621", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMicahChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22634", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMicahChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 22649", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;


    }

    public List<String> getVersesMicahChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 22665", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNahumChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22685", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNahumChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 22700", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesNahumChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 22713", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHabakkukChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 22732", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHabakkukChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 22749", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHabakkukChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 22769", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZephaniahChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 22788", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZephaniahChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22806", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZephaniahChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 22821", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHaggaiChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22841", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHaggaiChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 22856", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZechariahChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 22879", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZechariahChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 22900", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZechariahChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 22913", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZechariahChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 22923", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZechariahChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 22937", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZechariahChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 22948", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZechariahChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 22963", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZechariahChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 22977", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZechariahChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 23000", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZechariahChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 23017", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZechariahChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 23029", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZechariahChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 23046", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZechariahChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 9 OFFSET 23060", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesZechariahChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 23069", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMalachiChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 23090", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMalachiChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 23104", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMalachiChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 23121", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMalachiChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 6 OFFSET 23139", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 23145", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 23170", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 23193", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 23210", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 48 OFFSET 23235", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 23283", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 23317", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 23346", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 23380", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 23418", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 23460", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 50 OFFSET 23490", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 58 OFFSET 23540", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 23598", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 23634", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 23673", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 23701", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 23728", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 23763", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 23793", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 23827", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 23873", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 23919", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 51 OFFSET 23958", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 46 OFFSET 24009", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 75 OFFSET 24055", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 66 OFFSET 24130", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMatthewChapterTwentyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 24196", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMarkChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 45 OFFSET 24216", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMarkChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 24261", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMarkChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 24289", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMarkChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 41 OFFSET 26324", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMarkChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 24365", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMarkChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 56 OFFSET 24408", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMarkChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 24464", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMarkChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 24501", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMarkChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 50 OFFSET 24539", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMarkChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 52 OFFSET 24589", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMarkChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 24641", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMarkChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 24674", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMarkChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 24718", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMarkChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 72 OFFSET 24755", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMarkChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 47 OFFSET 24827", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesMarkChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 24874", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 80 OFFSET 24894", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 52 OFFSET 24974", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 25026", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 25064", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 25108", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 49 OFFSET 25147", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 50 OFFSET 25196", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 56 OFFSET 25246", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 62 OFFSET 25302", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 25364", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 54 OFFSET 25406", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 59 OFFSET 25460", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;


    }

    public List<String> getVersesLukeChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 25519", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 25554", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 25589", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 25621", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 25652", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 25689", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 48 OFFSET 25732", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 47 OFFSET 25780", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 25827", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 71 OFFSET 25865", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 56 OFFSET 25936", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesLukeChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 53 OFFSET 25992", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 51 OFFSET 26045", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 26096", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 26121", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 54 OFFSET 26157", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 47 OFFSET 26211", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 71 OFFSET 26258", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 53 OFFSET 26329", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 59 OFFSET 26382", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 41 OFFSET 26441", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 26482", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 57 OFFSET 26524", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 50 OFFSET 26581", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 26631", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 26669", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 26700", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 26727", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 26760", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 26786", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 26826", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 26868", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJohnChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 26899", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 26924", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 47 OFFSET 26950", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 26997", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 37 OFFSET 27023", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 42 OFFSET 27060", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 27102", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 60 OFFSET 27117", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 27177", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 43 OFFSET 27217", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 48 OFFSET 27260", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 27308", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 27338", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 52 OFFSET 27363", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 27415", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 41 OFFSET 27443", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 27484", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 27524", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;


    }

    public List<String> getVersesActsChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 27558", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 41 OFFSET 27586", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 38 OFFSET 27627", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 27665", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 27705", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterTwentyThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 35 OFFSET 27735", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterTwentyFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 27770", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterTwentyFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 27797", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterTwentySix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 27824", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterTwentySeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 44 OFFSET 27856", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesActsChapterTwentyEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 27900", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRomansChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 27931", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRomansChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 27963", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRomansChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 27992", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRomansChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 28023", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRomansChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 28048", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRomansChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 28069", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRomansChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 28092", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRomansChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 28117", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRomansChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 28156", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRomansChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 28189", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRomansChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 36 OFFSET 28210", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRomansChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 28246", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRomansChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 28267", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRomansChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 28281", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRomansChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 28304", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRomansChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 28337", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneCorinthiansChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 28364", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneCorinthiansChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 28395", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneCorinthiansChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 28411", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneCorinthiansChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 28434", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneCorinthiansChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 28455", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneCorinthiansChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 28468", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneCorinthiansChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 28488", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneCorinthiansChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 28528", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneCorinthiansChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 28541", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneCorinthiansChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 28568", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneCorinthiansChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 34 OFFSET 28601", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneCorinthiansChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 28635", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneCorinthiansChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 28666", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneCorinthiansChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 28679", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneCorinthiansChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 58 OFFSET 28719", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneCorinthiansChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 28777", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoCorinthiansChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 28801", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoCorinthiansChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 28825", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoCorinthiansChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 28842", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoCorinthiansChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 28860", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoCorinthiansChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 28878", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoCorinthiansChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 28899", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoCorinthiansChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 28917", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;


    }

    public List<String> getVersesTwoCorinthiansChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 28933", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoCorinthiansChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 28957", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoCorinthiansChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 28972", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoCorinthiansChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 28990", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoCorinthiansChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 29023", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoCorinthiansChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 29044", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGalatiansChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 29058", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGalatiansChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 29082", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGalatiansChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 29103", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGalatiansChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 31 OFFSET 29132", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGalatiansChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 29163", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesGalatiansChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 29189", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEphesiansChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 29207", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEphesiansChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 29230", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEphesiansChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 29252", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEphesiansChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 32 OFFSET 29273", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEphesiansChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 33 OFFSET 29305", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesEphesiansChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 29338", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPhilippiansChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 29362", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPhilippiansChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 30 OFFSET 29392", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPhilippiansChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 29422", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPhilippiansChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 29443", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesColossiansChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 29466", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesColossiansChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 23 OFFSET 29495", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesColossiansChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 29518", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesColossiansChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 29543", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneThessaloniansChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 29561", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneThessaloniansChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 29571", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneThessaloniansChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 29591", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneThessaloniansChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 29604", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneThessaloniansChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 29622", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoThessaloniansChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 12 OFFSET 29650", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoThessaloniansChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 29662", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoThessaloniansChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 29679", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneTimothyChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 29697", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneTimothyChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 29717", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneTimothyChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 29732", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneTimothyChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 29748", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneTimothyChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 29764", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneTimothyChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 29789", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoTimothyChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 29810", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoTimothyChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 29828", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoTimothyChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 29854", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoTimothyChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 29871", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTitusChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 29893", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTitusChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 29909", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTitusChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 29924", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesPhilemonChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 29939", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHebrewsChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 29964", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHebrewsChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 29978", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHebrewsChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 29996", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHebrewsChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 16 OFFSET 30015", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHebrewsChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 30031", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHebrewsChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 30045", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHebrewsChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 30065", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHebrewsChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 30093", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHebrewsChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 28 OFFSET 30106", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHebrewsChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 39 OFFSET 30134", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;


    }

    public List<String> getVersesHebrewsChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 40 OFFSET 30173", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHebrewsChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 30213", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesHebrewsChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 30242", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJamesChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 30267", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJamesChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 26 OFFSET 30294", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJamesChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 30320", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJamesChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 30338", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJamesChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 30355", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOnePeterChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 30375", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOnePeterChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 30400", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOnePeterChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 30425", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOnePeterChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 30447", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOnePeterChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 30466", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoPeterChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 30480", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoPeterChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 30501", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoPeterChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 30523", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneJohnChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 10 OFFSET 30541", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneJohnChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 30551", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneJohnChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 30580", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneJohnChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 30604", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesOneJohnChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 30625", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesTwoJohnChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 30646", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesThreeJohnChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 30659", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesJudeChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 25 OFFSET 30673", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 30698", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 29 OFFSET 30718", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterThree() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 22 OFFSET 30747", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterFour() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 30769", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterFive() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 14 OFFSET 30780", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterSix() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 30794", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterSeven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 30811", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterEight() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 13 OFFSET 30828", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterNine() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 30841", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterTen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 11 OFFSET 30862", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterEleven() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 19 OFFSET 30873", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterTwelve() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 17 OFFSET 30892", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterThirteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 30909", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterFourteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 20 OFFSET 30927", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterFifteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 8 OFFSET 30947", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterSixteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 30955", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterSeventeen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 18 OFFSET 30976", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterEighteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 24 OFFSET 30994", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterNineteen() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 31018", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterTwenty() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 15 OFFSET 31039", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterTwentyOne() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 27 OFFSET 31054", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getVersesRevelationChapterTwentyTwo() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull LIMIT 21 OFFSET 31081", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

  /*  public List<String> getDailyVerse() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM kjvbibledailyverse ORDER BY RANDOM() LIMIT 1", null);
        cursor.moveToFirst();
        while (cursor != null && cursor.getCount() > 0 & !cursor.isAfterLast()) {
            list.add(cursor.getString(3));
            list.add(cursor.getString(4));
            list.add(cursor.getString(5));
            list.add(cursor.getString(6));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }



    public void DailyVerseDatabaseAccess() {
        MainActivity mainActivity = new MainActivity();

        SQLiteOpenHelper sqLiteOpenHelper = new SQLiteAssetHelper(this, DATABASE_NAME, null, DATABASE_VERSION);
        mSQLiteDatabase = sqLiteOpenHelper.getReadableDatabase();
        mRawQuery = "SELECT * FROM kjvbibledailyverse ORDER BY RANDOM() LIMIT 1";
        mCursor = mSQLiteDatabase.rawQuery(mRawQuery, null);
        mDailyVerseCursorAdapter = new DailyVerseCursorAdapter(this, mCursor);

    }

   */

    public int deleteData(String name) {
        String whereArgs[] = {name};
        int count = database.delete(TABLE_NAME, BIBLE_FAVORITE_COLUMN_NAME + "=?", whereArgs);
        return count;
    }

    public long insertData(int favorite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BIBLE_FAVORITE_COLUMN_NAME, favorite);
        long id = database.insert(TABLE_NAME, null, contentValues);
        return id;
    }

    public Integer getId(int position) {
        int id = 0;
        Cursor cur = database.rawQuery("SELECT * FROM biblekjvcombinedfull", null);
        cur.moveToPosition(position);
        id = cur.getInt(1);
        cur.close();
        return id;
    }

    public List<String> getFavoriteVerses() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM biblekjvcombinedfull WHERE Field8 = 1", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(4));
            list.add(cursor.getString(5));
            list.add(cursor.getString(6));
            list.add(cursor.getString(7));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

}

