package com.errolapplications.bible365kjv;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class BibleApplication extends Application {

    public static Map<String, String> musicFilesUrls;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        String BIBLE_MUSIC_SHARED_PREF = "BibleMusicSharedPref";
        String BIBLE_MUSIC_MAP = "BibleMusicMap";

        Gson gson = new Gson();

        SharedPreferences musicSharedPref = getSharedPreferences(BIBLE_MUSIC_SHARED_PREF, MODE_PRIVATE);

        String musicFilesUrlsStr = musicSharedPref.getString(BIBLE_MUSIC_MAP, null);
        Log.d("PREPARING_AUDIO_EXCEPTION", "onCreate: " + musicFilesUrlsStr);

        if (musicFilesUrlsStr == null) {

            if (isNetworkConnected(getApplicationContext())) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference ref = database.getReference().child("music-files");

                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        musicFilesUrls = (Map<String, String>) snapshot.getValue();

                        String musicFilesUrlsStrToStore = gson.toJson(musicFilesUrls);
                        Log.d("PREPARING_AUDIO_EXCEPTION", String.valueOf(musicFilesUrls.size()));
                        musicSharedPref.edit().putString(BIBLE_MUSIC_MAP, musicFilesUrlsStrToStore).apply();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(),"No Internet",Toast.LENGTH_SHORT).show();
            }

        } else {

            Type type = new TypeToken<Map<String, String>>() {
            }.getType();
            musicFilesUrls = gson.fromJson(musicFilesUrlsStr, type);
            Log.d("PREPARING_AUDIO_EXCEPTION", String.valueOf(musicFilesUrls.size()));

        }

    }

    public static boolean isNetworkConnected(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            final Network n = cm.getActiveNetwork();

            if (n != null) {
                final NetworkCapabilities nc = cm.getNetworkCapabilities(n);

                assert nc != null;
                return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
            }
        }

        return false;
    }
}