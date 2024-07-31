package com.example.listerpros.repositiory

import com.example.listerpros.api.RetrofitInstance
import com.example.listerpros.model.updatejobstatus.UpdateJobStatus


class UpdateJobStatusRepository {

    fun updateJObStatus(id:String, status: String): retrofit2.Call<UpdateJobStatus>{
        return RetrofitInstance.api.updateJobStatus(id, status)
    }
}