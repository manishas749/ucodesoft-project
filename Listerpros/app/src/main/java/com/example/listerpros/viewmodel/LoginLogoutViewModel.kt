package com.example.listerpros.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.listerpros.model.login.ResponseLogin
import com.example.listerpros.repositiory.LoginLogoutRepository
import kotlinx.coroutines.launch
import retrofit2.Call

class LoginLogoutViewModel(application: Application) : AndroidViewModel(application) {
    val responseLogin: MutableLiveData<Call<ResponseLogin>> = MutableLiveData()

    private val loginLogoutRepository: LoginLogoutRepository = LoginLogoutRepository()

    fun loginResponse(email: String, password: String) {
        viewModelScope.launch {
            val response = loginLogoutRepository.login(email, password)
            responseLogin.value = response
        }
    }
}