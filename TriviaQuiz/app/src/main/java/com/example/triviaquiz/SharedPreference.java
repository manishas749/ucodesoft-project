package com.example.triviaquiz;

import static com.example.triviaquiz.LoginActivity.LOGGEDINUSER;
import static com.example.triviaquiz.QuizActivity.SAVEUSERSCOREHISTORY;
import static com.example.triviaquiz.QuizActivity.USERSCORE;
import static com.example.triviaquiz.RegisterationActivity.PREFERENCES;
import static com.example.triviaquiz.RegisterationActivity.SAVEDATA;
import static com.example.triviaquiz.RegisterationActivity.USERDATA;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Shared preference class to save the registered user
 * logged in user
 * logged in user score
 */
public class SharedPreference {
    private SharedPreferences prefs;

    public SharedPreference(Context context) {
        prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    public void saveUserData(ArrayList<SaveUserData> list) {
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(USERDATA, json);
        editor.apply();
    }

    public void saveUserScoreData(ArrayList<SaveUserGameScore> list) {
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(USERSCORE, json);
        editor.apply();
    }

    public void loadUserScoreData() {
        Gson gson = new Gson();
        String json = prefs.getString(USERSCORE, null);
        Type type = new TypeToken<ArrayList<SaveUserGameScore>>() {}.getType();
        if (json == null) {
            SAVEUSERSCOREHISTORY = new ArrayList<>();
        }
        if (json != null) {
            SAVEUSERSCOREHISTORY = gson.fromJson(json, type);
        }
    }



    public void loadUserData() {
        Gson gson = new Gson();
        String json = prefs.getString(USERDATA, null);
        Type type = new TypeToken<ArrayList<SaveUserData>>() {}.getType();
        if (json == null) {
            SAVEDATA = new ArrayList<>();
        }
        if (json != null) {
            SAVEDATA = gson.fromJson(json, type);
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