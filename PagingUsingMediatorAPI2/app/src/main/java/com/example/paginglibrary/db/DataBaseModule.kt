package com.example.paginglibrary.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataBaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context : Context): QuoteDataBase
    {
        return Room.databaseBuilder(context,QuoteDataBase::class.java,"newDB").fallbackToDestructiveMigration().build()

    }

    @Singleton
    @Provides
    fun provideQuoteDao(quoteDataBase: QuoteDataBase): QuoteDao {
        return quoteDataBase.quoteDao()
    }

}