package com.ucopdesoft.issuelogger.utils

import android.content.Context
import android.content.SharedPreferences
import com.ucopdesoft.issuelogger.utils.Constants.Companion.PREFERENCES
import com.ucopdesoft.issuelogger.utils.Constants.Companion.TOKEN

class UserDetailsPreference(context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)!!

    fun saveToken(token: String) {
        val editor = prefs.edit()
        editor.putString(TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        return prefs.getString(TOKEN, null)
    }

    fun clearToken() {
        val editor = prefs.edit()
        editor.remove(TOKEN)
        editor.apply()
    }
}