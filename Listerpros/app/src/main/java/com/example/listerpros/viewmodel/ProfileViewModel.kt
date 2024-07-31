package com.example.listerpros.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.listerpros.model.editprofile.ResponseEditProfile
import com.example.listerpros.model.getmyprofile.ResponseGetProfile
import com.example.listerpros.repositiory.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call

class ProfileViewModel(application: Application): AndroidViewModel(application) {

    val responseGetProfile: MutableLiveData<Call<ResponseGetProfile>> = MutableLiveData()
    val responseEditProfile: MutableLiveData<Call<ResponseEditProfile>> = MutableLiveData()
    private val profileRepository: ProfileRepository = ProfileRepository()

    fun getProfile(){
        viewModelScope.launch{
           val response = profileRepository.getProfile()
            responseGetProfile.value = response
        }
    }

    fun editProfile(firstName: String, lastName: String, email: String, cellPhone: String){
        viewModelScope.launch {
            val response = profileRepository.editProfile(firstName, lastName, email, cellPhone)
            responseEditProfile.value = response
        }
    }
}