package com.example.listerpros.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.listerpros.model.locationstatus.ResponseLocationStatus
import com.example.listerpros.repositiory.UpdateLocationStatusRepository
import kotlinx.coroutines.launch
import retrofit2.Call

class UpdateLocationStatusViewModel(application: Application):AndroidViewModel(application) {

    val responseUpdateLocationStatus: MutableLiveData<Call<ResponseLocationStatus>> = MutableLiveData()

    private val updateLocationStatusRepository: UpdateLocationStatusRepository = UpdateLocationStatusRepository()

    fun updateLocationStatusResponse(job_id: String, location_status: String) {
        viewModelScope.launch {
            val response = updateLocationStatusRepository.updateLocationStatus(job_id, location_status)
            responseUpdateLocationStatus.value = response
        }
    }
}