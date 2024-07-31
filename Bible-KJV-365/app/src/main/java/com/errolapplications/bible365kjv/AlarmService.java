package com.errolapplications.bible365kjv;

import static com.errolapplications.bible365kjv.TodaysVerseBroadcastReceiver.BIBLE_365_CHANNEL_ID;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.errolapplications.bible365kjv.model.DailyVerse;

import java.util.Calendar;
import java.util.Objects;

public class AlarmService extends Service {

    public boolean mVerseNotificationBoolean;

    public boolean DEBUG_NOTIFICATIONS = true;

    private DatabaseAccess databaseAccess;

    public AlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startAlarm(true, true);

        int REQUEST_CODE = 365;
        int NOTIFY_ID = 365;

        SharedPreferences notificationsSwitchState = getSharedPreferences("SettingsActivity", 0);
        mVerseNotificationBoolean = notificationsSwitchState.getBoolean("NotificationsSwitchState", true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Bible 365 Notification Channel";
            String description = "Channel for Bible 365";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(BIBLE_365_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }

        if (MainActivity.getInstance() != null) {

            if (mVerseNotificationBoolean) {

                Log.i("WWW", "Received notification message.");

                final DailyVerse verseToUse = MainActivity.getInstance().getAVerse(false);
                // fire notification
                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BIBLE_365_CHANNEL_ID);
                Intent notificationIntent = new Intent(this, MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(this, REQUEST_CODE, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
                Bitmap largeIcon = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_launcher);
                builder.setContentIntent(contentIntent);
                builder.setLargeIcon(largeIcon);
                builder.setSmallIcon(R.drawable.ic_stat_app_icon_transparent);
                builder.setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE);
                builder.setContentText(verseToUse.getNotificationString());
                builder.setContentTitle(verseToUse.getVerseTitleString());
                builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
                // builder.setChannelId(BIBLE_365_CHANNEL_ID);

                builder.setAutoCancel(true);

                Notification notification = builder.build();

                if (nm != null) {
                    nm.notify(NOTIFY_ID, notification);
                }

                MainActivity.getInstance().runOnUiThread(() -> MainActivity.getInstance().updateDailyVerseInActivity(verseToUse));

            } else {

                final DailyVerse verseToUse = MainActivity.getInstance().getAVerse(false);
                MainActivity.getInstance().runOnUiThread(() -> MainActivity.getInstance().updateDailyVerseInActivity(verseToUse));
            }

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    private void startAlarm(boolean isNotification, boolean isRepeat) {


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        //THIS IS WHERE YOU SET NOTIFICATION TIME FOR CASES WHEN THE NOTIFICATION NEEDS TO BE RESCHEDULED
        Calendar firingCal = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();
        firingCal.setTimeInMillis(System.currentTimeMillis());
        firingCal.set(Calendar.HOUR_OF_DAY, 9); // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, 0); // Particular minute
        firingCal.set(Calendar.SECOND, 0); // particular second

        long intendedTime = firingCal.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        myIntent = new Intent(this, TodaysVerseBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, PendingIntent.FLAG_IMMUTABLE);


        if (DEBUG_NOTIFICATIONS) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);

        } else if (intendedTime >= currentTime) {

            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);

        } else {

            firingCal.add(Calendar.DAY_OF_YEAR, 1);
            intendedTime = firingCal.getTimeInMillis();

            alarmManager.setInexactRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);

        }
    }
}

