package com.example.cinemago;

import static com.example.cinemago.Constants.LOGGEDINUSER;
import static com.example.cinemago.Constants.PREFERENCES;
import static com.example.cinemago.Constants.USERDATA;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.cinemago.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SharedPreference {

    private SharedPreferences prefs;
    public static User userData  = new User();


    public SharedPreference(Context context) {
        prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    public void saveUserData(User user) {
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(USERDATA, json);
        editor.apply();
    }

    public void loadUserData() {
        Gson gson = new Gson();
        String json = prefs.getString(USERDATA, null);
        Type type = new TypeToken<User>() {}.getType();
        if (json == null) {
            userData = new User();
        }
        if (json != null) {
            userData = gson.fromJson(json, type);
        }
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LOGGEDINUSER, token);
        editor.apply();
    }

    public String getToken(){
        return prefs.getString(LOGGEDINUSER, null);
    }

    public void clearToken() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(LOGGEDINUSER);
        editor.apply();

    }
}
