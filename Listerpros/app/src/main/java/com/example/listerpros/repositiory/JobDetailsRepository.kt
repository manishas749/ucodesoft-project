package com.example.listerpros.repositiory

import com.example.listerpros.api.RetrofitInstance
import com.example.listerpros.model.getjobdetails.ResponseJobDetails
import retrofit2.Call

class JobDetailsRepository {

    fun jobDetails(jobId: String): Call<ResponseJobDetails>
    {
        return RetrofitInstance.api.getJobDetails(jobId)
    }
}