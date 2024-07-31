package com.rentreadychecklist.preferences

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rentreadychecklist.constants.Constants.Companion.EMAIL
import com.rentreadychecklist.constants.Constants.Companion.PREFERENCES
import java.lang.reflect.Type

/**
 * This class is used for save and get emails from sharedPref entered by user in Edittext field.
 */
class SharedPreference(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)!!
    var emailList = arrayListOf<String>()


    // To save the emails entered in EditText
    fun saveEmail(List: ArrayList<String>) {
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(List)
        editor.putString(EMAIL, json)
        editor.apply()
    }

    //To load all the emails saved
    fun loadEmail() {
        val gson = Gson()
        val json: String? = prefs.getString(EMAIL, null)
        val type: Type = object : TypeToken<ArrayList<String>>() {}.type
        if (json == null) {
            emailList = ArrayList()
        }
        if (json != null) {
            emailList = gson.fromJson(json, type)
        }
    }


}