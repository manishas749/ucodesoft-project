package com.example.notes_app.repository

import androidx.lifecycle.LiveData
import com.example.notes_app.data.NotesDao
import com.example.notes_app.datamodel.Notes

class NotesRepository(private val notesDao: NotesDao) {

    val readAllNotes: LiveData<List<Notes>> = notesDao.readAllNotes()
   suspend fun addNotes(notes: Notes)
    {
        notesDao.addNotes(notes)
    }
    suspend fun deleteNotes(notes: Notes)
    {
      notesDao.deleteNote(notes)
    }
   suspend fun updateNotes(notes: Notes)
    {
        notesDao.updateNote(notes)
    }
    fun searchDatabase(searchQuery: String): kotlinx.coroutines.flow.Flow<List<Notes>> {
        return notesDao.searchData(searchQuery)
    }





}