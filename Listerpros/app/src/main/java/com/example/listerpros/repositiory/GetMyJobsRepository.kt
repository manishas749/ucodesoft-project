package com.example.listerpros.repositiory

import com.example.listerpros.api.RetrofitInstance
import com.example.listerpros.model.getmyjobs.GetMyJobs
import retrofit2.Call
import retrofit2.http.Query

class GetMyJobsRepository {

    fun getJobs(startDate: String, endDate:String): Call<GetMyJobs>{
           return RetrofitInstance.api.getJobs(startDate, endDate)
    }
}