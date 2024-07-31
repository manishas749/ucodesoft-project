package com.example.listerpros.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.listerpros.model.updatejobstatus.UpdateJobStatus
import com.example.listerpros.repositiory.UpdateJobStatusRepository
import kotlinx.coroutines.launch
import retrofit2.Call


class UpdateJobStatusViewModel(application: Application): AndroidViewModel(application) {
    val responseUpdateJobStatus : MutableLiveData<Call<UpdateJobStatus>> = MutableLiveData()
    private val repository = UpdateJobStatusRepository()

    fun updateJobStatus(id: String, status: String){
        viewModelScope.launch {
            val response = repository.updateJObStatus(id, status)
            responseUpdateJobStatus.value =  response
        }
    }
}