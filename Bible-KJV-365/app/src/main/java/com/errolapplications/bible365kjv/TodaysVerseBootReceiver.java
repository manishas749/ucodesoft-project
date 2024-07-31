package com.errolapplications.bible365kjv;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.Calendar;
import java.util.Objects;


public class TodaysVerseBootReceiver extends BroadcastReceiver {

    public boolean isWorkingBoot;
    public boolean isWorkingAlarm;
    private boolean DEBUG_NOTIFICATIONS = false;
    private boolean isWorking;
    private int ALARM_ID_FROM_MAIN_ACT;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences mBootPrefs;

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals((intent.getAction()))) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            ALARM_ID_FROM_MAIN_ACT = prefs.getInt("ALARM_ID_SHARED_PREFS", 0);

            int ALARM_ID = 1000;
            int ALARM2_ID = 1001;

            Intent intent2 = new Intent(context, TodaysVerseBroadcastReceiver.class);
            intent2.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ALARM_ID_FROM_MAIN_ACT, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
            isWorkingBoot = (PendingIntent.getBroadcast(context, ALARM_ID_FROM_MAIN_ACT, intent2, PendingIntent.FLAG_NO_CREATE | PendingIntent.FLAG_IMMUTABLE) != null);//just changed the flag
            AlarmManager alarmManager = (AlarmManager) (context.getSystemService(Context.ALARM_SERVICE));
            Objects.requireNonNull(alarmManager).cancel(pendingIntent);

            Calendar firingCal = Calendar.getInstance();
            Calendar currentCal = Calendar.getInstance();
            firingCal.setTimeInMillis(System.currentTimeMillis());
            firingCal.set(Calendar.HOUR_OF_DAY, 9); // At the hour you wanna fire
            firingCal.set(Calendar.MINUTE, 0); // Particular minute
            firingCal.set(Calendar.SECOND, 0); // particular second

            long intendedTime = firingCal.getTimeInMillis();
            long currentTime = currentCal.getTimeInMillis();

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

        Log.i("TAG", "BOOT Received notification message.");

    }
}




