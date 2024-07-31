package com.example.listerpros.repositiory

import com.example.listerpros.api.RetrofitInstance
import com.example.listerpros.model.updatetaskstatus.UpdateTaskStatus
import retrofit2.Call

class UpdateTaskStatusRepository {

    fun updateTaskStatus(id:String, notes: String, status: String): Call<UpdateTaskStatus>{
        return RetrofitInstance.api.updateTaskStatus(id, notes, status)
    }
}