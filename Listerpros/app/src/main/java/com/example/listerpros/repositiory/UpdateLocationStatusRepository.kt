package com.example.listerpros.repositiory

import com.example.listerpros.api.RetrofitInstance
import com.example.listerpros.model.locationstatus.ResponseLocationStatus
import retrofit2.Call

class UpdateLocationStatusRepository {

    fun updateLocationStatus(job_id: String, location_status: String): Call<ResponseLocationStatus> {
        return RetrofitInstance.api.updateLocationStatus(job_id, location_status)
    }
}