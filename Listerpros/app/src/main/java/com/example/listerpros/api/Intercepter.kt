package com.example.listerpros.api

import android.util.Log
import com.example.listerpros.constants.Constants.Companion.BEARER_TOKEN
import okhttp3.Interceptor
import okhttp3.Response


class Interceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d("encoded",BEARER_TOKEN)

        val request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .addHeader("Authorization", "Bearer $BEARER_TOKEN")
            .addHeader("time-zone", "Asia/Kolkata")
            .addHeader("Accept" ,"application/json")
            .build()

        return chain.proceed(request)
    }

}