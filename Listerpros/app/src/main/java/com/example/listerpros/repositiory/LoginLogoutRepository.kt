package com.example.listerpros.repositiory

import com.example.listerpros.api.RetrofitInstance
import com.example.listerpros.model.login.ResponseLogin
import retrofit2.Call

class LoginLogoutRepository {
    fun login(email: String, password: String): Call<ResponseLogin> {
        return RetrofitInstance.api.login(email, password)
    }
}