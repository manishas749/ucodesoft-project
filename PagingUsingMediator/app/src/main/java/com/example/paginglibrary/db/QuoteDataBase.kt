package com.example.paginglibrary.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.paginglibrary.models.Product
import com.example.paginglibrary.models.QuoteRemoteKeys
import com.google.gson.Gson

@Database(entities = [Product::class,QuoteRemoteKeys::class], version = 2)
abstract class QuoteDataBase :RoomDatabase() {

    abstract fun quoteDao():QuoteDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}

