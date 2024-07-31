package com.example.anew.retrofit

import com.example.paginglibrary.models.NewsList
import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteAPI {

    @GET("/v2/everything?q=apple&from=2023-07-27&to=2023-07-27&sortBy=popularity&apiKey=c94eef505d944775ae1168e256b7e58c")
    suspend fun getQuotes(@Query("page") page: Int): NewsList
}