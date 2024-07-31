package com.errolapplications.bible365kjv;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.errolapplications.bible365kjv.model.DailyVerse;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.List;
import java.util.Objects;

public class TodaysVerseBroadcastReceiver extends BroadcastReceiver {

    public static final String SHARED_PREFS = "shared_preferences";
    public static final String SHARED_PREFS2 = "shared_preferences_2";

    public boolean isWorkingBoot;
    private int ALARM_ID_FROM_MAIN_ACT;
    private boolean mVerseNotificationBoolean;
    private static SharedPreferences firstRunPrefs;
    public static final String TODAYS_VERSE_STRING = "todays_verse_string";
    public static final String TODAYS_BOOK_NAME = "todays_book_name_string";
    public static final String TODAYS_CHAPTER_NUMBER = "todays_chapter_number_string";
    public static final String TODAYS_VERSE_ID_COLUMN_NAME = "_id";
    public static final String TODAYS_VERSE_VERSE_COLUMN_NAME = "verse";
    public static final String TODAYS_VERSE_BOOK_MAIN_COLUMN_NAME = "book_main";
    public static final String TODAYS_VERSE_CHAPTER_NUM_COLUMN_NAME = "chapter_number";
    public static final String TODAYS_VERSE_VERSE_NUM_COLUMN_NAME = "verse_number";
    public static final String BIBLE_365_CHANNEL_ID = "todays_verse_notifications";
    private static final String DATABASE_NAME = "bible365db111718.db";
    private static SharedPreferences getAVersePrefs;
    final String PREF_VERSION_CODE_KEY = "version_code";
    final int DOESNT_EXIST = -1;
    DatabaseAccess databaseAccess;
    public static final String CHANNEL_ID = "01";
    MainActivity mainActivity = MainActivity.getInstance();
    private String TODAYS_VERSE_NUMBER = "com.errolapplications.bible365.todays_verse_number";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteDatabase mSQLiteDatabase;
    private static SQLiteOpenHelper mSQLiteOpenHelper;
    private String TODAYS_VERSE = "com.errolapplications.bible365.todays_verse";
    private Cursor mCursor;
    public String FIRST_RUN_STATUS = "first_run_status";
    private boolean mFirstRunStatusBoolean;
    private int savedVersionCode;
    private int currentVersionCode = BuildConfig.VERSION_CODE;
    private String TAG = "TAG";

    @Override
    public void onReceive(Context context, Intent intent) {

        int REQUEST_CODE = 365;
        int NOTIFY_ID = 365;

        SharedPreferences notificationsSwitchState = context.getSharedPreferences("SettingsActivity", 0);
        mVerseNotificationBoolean = notificationsSwitchState.getBoolean("NotificationsSwitchState", true);
        getAVersePrefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        firstRunPrefs = context.getSharedPreferences(SHARED_PREFS2, MODE_PRIVATE);
        savedVersionCode = firstRunPrefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);
        firstRunPrefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();

        mSQLiteOpenHelper = new SQLiteAssetHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        mSQLiteDatabase = mSQLiteOpenHelper.getReadableDatabase();
        final DailyVerse verseToUse1 = getAVerse(false);
        final DailyVerse verseToUse2 = getAVerse(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Bible 365 Notification Channel";
            String description = "Channel for Bible 365";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(BIBLE_365_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }

        if (mVerseNotificationBoolean) {

            Log.i("TAG", "Received notification message.");

            // fire notification
            NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, BIBLE_365_CHANNEL_ID);
            Intent notificationIntent = new Intent(context, SplashActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, REQUEST_CODE, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
            builder.setContentIntent(contentIntent);
            builder.setLargeIcon(largeIcon);
            builder.setSmallIcon(R.drawable.ic_stat_app_icon_transparent);
            builder.setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE);
            builder.setContentText(verseToUse1.getNotificationString());
            builder.setContentTitle(verseToUse1.getVerseTitleString());
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            builder.setAutoCancel(true);


            Notification notification = builder.build();

            if (nm != null) {
                nm.notify(NOTIFY_ID, notification);
            }


            if (MainActivity.getInstance() != null) {
                MainActivity.getInstance().runOnUiThread(() -> mainActivity.updateDailyVerseInActivity(verseToUse1));
            }
        }

        if (!mVerseNotificationBoolean) {

            if (MainActivity.getInstance() != null) {
                MainActivity.getInstance().runOnUiThread(() -> mainActivity.updateDailyVerseInActivity(verseToUse2));
            }

        }
    }


    public DailyVerse getAVerse(boolean usingCachedIsOk) {

        int dailyVerseRowId = getAVersePrefs.getInt(TODAYS_VERSE_STRING, 0);

        String rawQuery = "SELECT * FROM kjvbibledailyverse ORDER BY RANDOM() LIMIT 1";
        String[] selectionArgs = null;
        if (dailyVerseRowId != 0 && usingCachedIsOk) {
            Log.d(TAG, "***********************************************************Using cached verse.");
            rawQuery = "SELECT * FROM kjvbibledailyverse where _id == ?";
            selectionArgs = new String[]{String.valueOf(dailyVerseRowId)};
        }

        mCursor = mSQLiteDatabase.rawQuery(rawQuery, selectionArgs);

        Log.d(TAG, "Rows returned " + (mCursor.getCount()));
        Log.d(TAG, "Column Names " + mCursor.getColumnNames()[4]);
        Log.d(TAG, "Column index for verse " + (mCursor.getColumnIndex("verse")));
        Log.d(TAG, "Column count " + (mCursor.getColumnCount()));

        int verseIndex = 0;
        int bookNameIndex = 0;
        int chapterNumberIndex = 0;
        int verseNumberIndex = 0;

        String verseString = "";
        String bookNameString = "";
        String chapterNumberString = "";
        String verseNumberString = "";

        int idColIndex = mCursor.getColumnIndex(TODAYS_VERSE_ID_COLUMN_NAME);
        int idxVerse = mCursor.getColumnIndex(TODAYS_VERSE_VERSE_COLUMN_NAME);
        int idxBookName = mCursor.getColumnIndex(TODAYS_VERSE_BOOK_MAIN_COLUMN_NAME);
        int idxChapterNumber = mCursor.getColumnIndex(TODAYS_VERSE_CHAPTER_NUM_COLUMN_NAME);
        int idxVerseNumber = mCursor.getColumnIndex(TODAYS_VERSE_VERSE_NUM_COLUMN_NAME);

        while (mCursor.moveToNext()) {
            verseIndex = mCursor.getInt(idColIndex);
            verseString = mCursor.getString(idxVerse);
            bookNameIndex = mCursor.getInt(idxBookName);
            bookNameString = mCursor.getString(idxBookName);
            chapterNumberIndex = mCursor.getInt(idxChapterNumber);
            chapterNumberString = mCursor.getString(idxChapterNumber);
            verseNumberIndex = mCursor.getInt(idxVerseNumber);
            verseNumberString = mCursor.getString(idxVerseNumber);

        }

        SharedPreferences.Editor editor = getAVersePrefs.edit();
        editor.putInt(TODAYS_VERSE_STRING, verseIndex);
        editor.putInt(TODAYS_BOOK_NAME, bookNameIndex);
        editor.putInt(TODAYS_CHAPTER_NUMBER, chapterNumberIndex);
        editor.putInt(TODAYS_VERSE_NUMBER, verseNumberIndex);
        editor.apply();

        return new DailyVerse(mCursor, bookNameString, chapterNumberString, verseNumberString, verseString);
    }

    private boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }
        final String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }
}
















