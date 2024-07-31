package com.example.listerpros.repositiory

import com.example.listerpros.api.RetrofitInstance
import com.example.listerpros.model.addnote.ResponseJobNotes
import com.example.listerpros.model.addnotesmodified.ResponseAddNoteModified
import retrofit2.Call

class JobNotesRepository {

    fun jobNotes(job_id: String, notes: String): Call<ResponseJobNotes> {
        return RetrofitInstance.api.jobNotes(job_id, notes)
    }

    fun addNotesModified(job_id: String, notes: String,id:String): Call<ResponseAddNoteModified> {
        return RetrofitInstance.api.addNotesModified(job_id, notes,id)
    }
}