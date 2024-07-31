package com.example.listerpros.repositiory

import com.example.listerpros.api.RetrofitInstance
import com.example.listerpros.model.editprofile.ResponseEditProfile
import com.example.listerpros.model.getmyprofile.ResponseGetProfile
import retrofit2.Call

class ProfileRepository {

    fun getProfile(): retrofit2.Call<ResponseGetProfile> {
        return RetrofitInstance.api.getProfile()
    }

    fun editProfile(firstName: String, lastName: String, email: String, cellPhone: String): Call<ResponseEditProfile> {
        return RetrofitInstance.api.editProfile(firstName, lastName, email, cellPhone)
    }
}