package com.example.paginglibrary.retrofit

import com.example.paginglibrary.models.QuoteList
import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteAPI {

    @GET("/products")
    suspend fun getQuotes(@Query("page") page: Int): QuoteList
}