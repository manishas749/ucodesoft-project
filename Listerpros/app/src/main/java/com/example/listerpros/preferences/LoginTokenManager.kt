package com.example.listerpros.preferences

import android.content.Context
import com.example.listerpros.constants.Constants.Companion.TOKEN

class LoginTokenManager(context: Context)
{
    private val timeSheet=TimeSheetTokenManager(context)

    fun saveToken(token:String)
    {
        val editor=timeSheet.prefs.edit()
        editor.putString(TOKEN,token)
        editor.apply()
    }

    fun getToken(): String?
    {
        return timeSheet.prefs.getString(TOKEN, null)
    }

    fun clearToken()
    {
         val editor=timeSheet.prefs.edit()
         editor.remove(TOKEN)
         editor.apply()
    }

}