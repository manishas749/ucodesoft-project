package com.example.listerpros.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.listerpros.model.getmyjobs.GetMyJobs
import com.example.listerpros.repositiory.GetMyJobsRepository
import kotlinx.coroutines.launch
import retrofit2.Call

class GetMyJobsViewModel(application: Application): AndroidViewModel(application) {

    val responseGetMyJobs: MutableLiveData<Call<GetMyJobs>> = MutableLiveData()
    private val getMyJobsRepository: GetMyJobsRepository = GetMyJobsRepository()

    fun getJobs(startDate: String, endDate:String){
        viewModelScope.launch {
            val response = getMyJobsRepository.getJobs(startDate, endDate)
            responseGetMyJobs.value = response
        }
    }
}