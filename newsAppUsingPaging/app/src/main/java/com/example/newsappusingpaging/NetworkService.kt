package com.example.newsappusingpaging

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService
{
    @GET("/v2/everything?q=apple&from=2023-07-27&to=2023-07-27&sortBy=popularity&apiKey=c94eef505d944775ae1168e256b7e58c")
    fun getNews(@Query("page") page: Int, @Query("pageSize") pageSize: Int): Single<Response>

    companion object {
        fun getService(): NetworkService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(NetworkService::class.java)
        }
    }


}