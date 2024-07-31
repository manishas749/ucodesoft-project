package com.example.paginglibrary.di


import com.example.paginglibrary.retrofit.QuoteAPI
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
        return Retrofit.Builder().baseUrl("https://newsapi.org/v2/").addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    @Provides
    @Singleton
    fun getQuoteAPI(retrofit: Retrofit): QuoteAPI
    {
        return retrofit.create(QuoteAPI::class.java)
    }


}