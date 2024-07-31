package com.example.listerpros.preferences

import android.content.Context
import android.content.SharedPreferences
import com.example.listerpros.constants.Constants.Companion.COUNTING_KEY
import com.example.listerpros.constants.Constants.Companion.CURRENT_DATE
import com.example.listerpros.constants.Constants.Companion.PREFERENCES
import com.example.listerpros.constants.Constants.Companion.START_TIME_KEY
import com.example.listerpros.constants.Constants.Companion.STOP_TIME_KEY
import java.text.SimpleDateFormat
import java.util.*

// shared preference for time sheet
class TimeSheetTokenManager(context: Context) {

    var prefs: SharedPreferences =
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)!!

    private var dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault())
    private var startTime: Date? = null
    private var stopTime: Date? = null
    private var timeCounting = false


    init {

        timeCounting = prefs.getBoolean(COUNTING_KEY, false)
        val startString = prefs.getString(START_TIME_KEY, null)
        if (startString != null) {
            startTime = dateFormat.parse(startString)
        }
        val stopString = prefs.getString(STOP_TIME_KEY, null)
        if (stopString != null) {
            stopTime = dateFormat.parse(stopString)
        }

    }

    fun startTime(): Date? = startTime

    fun setStartTime(date: Date?)                // save start time and date
    {
        startTime = date
        with(prefs.edit())
        {
            val stringDate = if (date == null) null
            else
                dateFormat.format(date)

            putString(START_TIME_KEY, stringDate)
            apply()
        }
    }

    fun startTimeClear() {
        val editor = prefs.edit()
        editor.remove(START_TIME_KEY)
        editor.apply()
    }

    fun saveCurrentDate(date: String) {
        val editor = prefs.edit()
        editor.putString(CURRENT_DATE, date)
        editor.apply()
    }


    fun stopTime(): Date? = stopTime

    fun setStopTime(date: Date?)              //to  save stop time and date
    {
        stopTime = date
        with(prefs.edit())
        {
            val stringDate = if (date == null) null else dateFormat.format(date)
            putString(STOP_TIME_KEY, stringDate)
            apply()
        }
    }

    fun stopTimeClear() {
        val editor = prefs.edit()
        editor.remove(STOP_TIME_KEY)
        editor.apply()
    }

    fun timeCounting(): Boolean = timeCounting               // difference in time

    fun setTimerCounting(value: Boolean)                   // time difference
    {
        timeCounting = value
        with(prefs.edit())
        {
            putBoolean(COUNTING_KEY, value)
            apply()
        }

    }

    fun countingTimeClear() {
        val editor = prefs.edit()
        editor.remove(COUNTING_KEY)
        editor.apply()
    }
}