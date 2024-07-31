package com.example.paginglibrary.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.paginglibrary.models.Article
import com.example.paginglibrary.models.QuoteRemoteKeys
import com.example.paginglibrary.models.RoomTypeConvertor

@Database(entities = [Article::class,QuoteRemoteKeys::class], version = 2)
@TypeConverters(RoomTypeConvertor::class)
abstract class QuoteDataBase :RoomDatabase() {

    abstract fun quoteDao():QuoteDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}

