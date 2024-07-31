package com.example.paginglibrary.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paginglibrary.models.Article
import com.example.paginglibrary.models.QuoteRemoteKeys


@Dao
interface QuoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(list: List<Article>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticleSingle(article: Article)

    @Query("SELECT * FROM Quote ")
    fun getAllArticles(): PagingSource<Int, Article>

    @Query("DELETE FROM Quote")
    suspend fun deleteAllArticles()


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRemoteKeys(list: List<QuoteRemoteKeys>)


    @Query("SELECT * FROM QuoteRemoteKeys WHERE id = :id")
    suspend fun getAllREmoteKey(id: String): QuoteRemoteKeys?

    @Query("DELETE FROM QuoteRemoteKeys")
    suspend fun deleteAllRemoteKeys()

}