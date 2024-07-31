package com.example.listerpros.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.listerpros.model.updatetaskstatus.UpdateTaskStatus
import com.example.listerpros.repositiory.UpdateTaskStatusRepository
import kotlinx.coroutines.launch
import retrofit2.Call

class UpdateTaskStatusViewModel(application: Application):AndroidViewModel(application) {

    val responseUpdateTaskStatus: MutableLiveData<Call<UpdateTaskStatus>> = MutableLiveData()
    val repository = UpdateTaskStatusRepository()

    fun updateTaskStatus(id: String, notes:String, status: String){
        viewModelScope.launch {
            val response = repository.updateTaskStatus(id, notes, status)
            responseUpdateTaskStatus.value = response
        }
    }
}