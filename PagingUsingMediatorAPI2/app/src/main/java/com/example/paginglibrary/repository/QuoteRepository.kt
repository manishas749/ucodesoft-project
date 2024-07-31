package com.example.paginglibrary.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.paginglibrary.db.QuoteDao
import com.example.paginglibrary.retrofit.QuoteAPI
import com.example.paginglibrary.models.Article
import com.example.paginglibrary.paging.QuotePagingSource
import javax.inject.Inject

@ExperimentalPagingApi
class QuoteRepository @Inject constructor(private val quoteAPI: QuoteAPI, private val quoteDao:QuoteDao) {




    fun getAllNewsStream(): LiveData<PagingData<Article>> = Pager(
        config = PagingConfig(
            20,
            5,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {
            QuotePagingSource(quoteAPI)
        }
    ).liveData

}




