package com.example.listerpros.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.listerpros.model.getjobdetails.ResponseJobDetails
import com.example.listerpros.repositiory.JobDetailsRepository
import kotlinx.coroutines.launch
import retrofit2.Call

class JobDetailsViewModel(application: Application): AndroidViewModel(application) {

    val responseJobDetails : MutableLiveData<Call<ResponseJobDetails>> = MutableLiveData()
    private val jobDetailsRepository = JobDetailsRepository()

    fun jobDetails(jobId:String)
    {
        viewModelScope.launch {
            val response = jobDetailsRepository.jobDetails(jobId)
            responseJobDetails.value = response
        }
    }

}