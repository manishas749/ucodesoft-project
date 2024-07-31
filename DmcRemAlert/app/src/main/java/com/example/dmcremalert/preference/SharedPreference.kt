package com.example.dmcremalert.preference

import android.content.Context
import android.content.SharedPreferences
import com.example.dmcremalert.Constants.Companion.PREFERENCES
import com.example.dmcremalert.Constants.Companion.device
import com.example.dmcremalert.Constants.Companion.deviceName

/**
 *    This class is used for save and get emails from sharedPref entered by user in Edittext field.
 */
class SharedPreference(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)!!


    fun setSelectedDeviceAddress(value:String)
    {
        val editor = prefs.edit()
        editor.putString(device,value)
        editor.apply()
    }

    fun getSelectedDeviceAddress(): String? {

        return prefs.getString(device, null)

    }

    fun setSelectedDeviceName(value:String)
    {
        val editor = prefs.edit()
        editor.putString(deviceName,value)
        editor.apply()
    }

    fun getSelectedDeviceName():String?
    {
        return prefs.getString(deviceName, null)

    }


}