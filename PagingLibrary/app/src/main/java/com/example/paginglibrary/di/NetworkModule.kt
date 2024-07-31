package com.example.paginglibrary.di

import com.example.anew.Constants.Companion.BASE_URL
import com.example.anew.retrofit.QuoteAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun getRetrofit():Retrofit
    {
        return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    @Provides
    @Singleton
    fun getQuoteAPI(retrofit: Retrofit):QuoteAPI
    {
        return retrofit.create(QuoteAPI::class.java)
    }
}