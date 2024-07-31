package com.example.notes_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.notes_app.data.NotesDatabase
import com.example.notes_app.datamodel.Notes
import com.example.notes_app.repository.NotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {

    val readAllNotes: LiveData<List<Notes>>
    private val repository: NotesRepository


    init {
        val notesDao = NotesDatabase.getDatabase(application).NotesDao()
        repository = NotesRepository(notesDao)
        readAllNotes = repository.readAllNotes
    }

    fun addNotes(notes: Notes) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addNotes(notes)
        }
    }

    fun deleteNotes(notes: Notes) {
        viewModelScope.launch(Dispatchers.IO) {

            repository.deleteNotes(notes)
        }
    }

    fun updateNotes(notes: Notes) {
        viewModelScope.launch(Dispatchers.IO) {

            repository.updateNotes(notes)
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Notes>> {
        return repository.searchDatabase(searchQuery).asLiveData()
    }
}




