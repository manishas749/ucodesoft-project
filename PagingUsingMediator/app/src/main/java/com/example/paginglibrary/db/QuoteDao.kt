package com.example.paginglibrary.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paginglibrary.models.Product


@Dao
interface QuoteDao {

    @Query("DELETE FROM Quote")
    suspend fun deleteQuote()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addQuotes(quotes:List<Product>)

    @Query("SELECT * FROM Quote")
    fun getQuotes():PagingSource<Int,Product>

}