package com.example.listerpros.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.listerpros.constants.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.math.abs

class Helper {


    companion object {
        //function to validate email format
        fun isValidEmail(email: String): Boolean {

            return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

       //function to check if email format is correct
        fun isValidEmailFormat(email: String): Boolean {
            return Pattern.compile(
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
            ).matcher(email).matches()
        }

        //function to hide the keyboard
        fun hideKeyBoard(view: View) {
            val hide =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hide.hideSoftInputFromWindow(view.windowToken, 0)
        }

        //to get the current date format to display on fragments
         fun getCurrentDate():ArrayList<String>
        {

            val getCurrentList = ArrayList<String>()
            val calender = Calendar.getInstance()
            val day = calender.get(Calendar.DAY_OF_MONTH)
            val currentDayDate = SimpleDateFormat("dd", Locale.getDefault()).format(Date())
            val currentDay = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
            val currentMonth = SimpleDateFormat("MMM", Locale.getDefault()).format(Date())
            val currentYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(Date())
            val yearMonth=("$currentMonth, $currentYear")

            val dateFormat = SimpleDateFormat("MMMM dd/yyyy h:mm a", Locale.getDefault()).format(
                Date()
            )

            getCurrentList.add(currentDayDate.toString())
            getCurrentList.add(currentDay.toString())
            getCurrentList.add(yearMonth)
            getCurrentList.add(dateFormat.toString())

            return getCurrentList

        }

        fun timeDiffernce(startTime: String, endTime: String): Pair<String,Long>{
            var result = ""
            var differenceInMilliSeconds:Long=0

            try {

                val formatter = SimpleDateFormat("HH:mm:ss", Locale.US)
                val startDateFormat = formatter.parse(startTime)

                val endDateFormat = formatter.parse(endTime)

                differenceInMilliSeconds = abs(startDateFormat.time - endDateFormat.time)

                result = hourMinuteCalculation(differenceInMilliSeconds)

            } catch (e: Exception) {
                Log.d("exception", e.toString())
            }
            return Pair(result,differenceInMilliSeconds)
        }


        fun hourMinuteCalculation(differenceInMilliSeconds : Long): String{
            var result = ""
            val differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)
                    % 24)
            val differenceInHoursFormat = "" + differenceInHours + "h"
            val differenceInMinutes = differenceInMilliSeconds / (60 * 1000) % 60
            val differenceInMinutesFormat = "" + differenceInMinutes + "m"
            val differenceInSeconds = differenceInMilliSeconds / 1000 % 60
            val differenceInSecondsFormat = "" + differenceInSeconds + "s"
            if (differenceInHours > 0 && differenceInMinutes > 0) {
                "$differenceInHoursFormat:$differenceInMinutesFormat:$differenceInSeconds".also {
                    result = it
                }
            } else {
                "$differenceInMinutesFormat:$differenceInSecondsFormat".also {
                    result = it
                }
            }

            return result
        }





    }

}