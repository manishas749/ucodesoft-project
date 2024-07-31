package com.example.notes_app.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.notes_app.datamodel.Notes

@Dao
interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)            //Insert Query
    suspend fun addNotes(notes : Notes)

    @Delete                                                   //Delete Query
    suspend fun deleteNote(notes: Notes)

    @Update                                                  //Update Query
    suspend fun updateNote(notes: Notes)

    @Query("SELECT * FROM Notes_Table ORDER BY id ASC")               //All notes read Query
    fun  readAllNotes():LiveData<List<Notes>>

    @Query("SELECT * FROM Notes_Table WHERE title LIKE :searchQuery OR description LIKE :searchQuery")
    fun searchData(searchQuery: String): kotlinx.coroutines.flow.Flow<List<Notes>>
    }                                            //Search Query