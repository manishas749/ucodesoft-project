package com.example.listerpros.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.listerpros.model.addnote.ResponseJobNotes
import com.example.listerpros.model.addnotesmodified.ResponseAddNoteModified
import com.example.listerpros.repositiory.JobNotesRepository
import kotlinx.coroutines.launch
import retrofit2.Call

class JobNotesViewModel(application: Application):AndroidViewModel(application) {

    val responseJobNotes: MutableLiveData<Call<ResponseJobNotes>> = MutableLiveData()
    val responseaddNotesModified: MutableLiveData<Call<ResponseAddNoteModified>> = MutableLiveData()
    private val jobNotesRepository:JobNotesRepository = JobNotesRepository()

    fun jobNotes(job_id: String, notes: String) {
        viewModelScope.launch {
            val response = jobNotesRepository.jobNotes(job_id, notes)
            responseJobNotes.value = response
        }
    }

    fun addNotesModified(job_id: String, notes: String,id: String) {
        viewModelScope.launch {
            val response = jobNotesRepository.addNotesModified(job_id, notes,id)
            responseaddNotesModified.value = response
        }
    }



}