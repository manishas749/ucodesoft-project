package com.example.fragmentsproject.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.fragmentsproject.data.constants.Companion.PREFERENCES
import com.example.fragmentsproject.data.constants.Companion.TOKEN

class SharedPreference(var context: Context) {

    private var prefs:SharedPreferences = context.getSharedPreferences(PREFERENCES,Context.MODE_PRIVATE)

    fun saveToken(token:String)
    {
        val editor = prefs.edit()
        editor.putString(TOKEN,token)
        editor.apply()
    }


    fun getToken(): String?
    {
        return prefs.getString(TOKEN,null)

    }

    fun clearToken()
    {
        val editor = prefs.edit()
        editor.remove(TOKEN)
        editor.apply()
    }



}