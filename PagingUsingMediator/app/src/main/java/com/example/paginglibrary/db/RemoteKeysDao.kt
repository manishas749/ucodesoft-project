package com.example.paginglibrary.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paginglibrary.models.QuoteRemoteKeys


@Dao
interface RemoteKeysDao {

    @Query("DELETE FROM QuoteRemoteKeys")
    suspend fun deleteAllRemoteKeys()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys:List<QuoteRemoteKeys>)
 
    @Query("SELECT * FROM QuoteRemoteKeys where id= :id")
    fun getRemoteKeys(id: Int): QuoteRemoteKeys

}