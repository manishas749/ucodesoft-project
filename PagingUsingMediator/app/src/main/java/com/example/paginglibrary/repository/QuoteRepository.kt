package com.example.paginglibrary.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.paginglibrary.db.QuoteDao
import com.example.paginglibrary.paging.QuotePagingSource
import com.example.paginglibrary.retrofit.QuoteAPI
import com.example.paginglibrary.db.QuoteDataBase
import com.example.paginglibrary.db.QuoteRemoteMediator
import com.example.paginglibrary.models.Product
import javax.inject.Inject

@ExperimentalPagingApi
class QuoteRepository @Inject constructor(private val quoteAPI: QuoteAPI, private val quoteDataBase: QuoteDataBase) {


    fun getQuotes() = Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = true),
        remoteMediator = QuoteRemoteMediator(quoteAPI,quoteDataBase),
        pagingSourceFactory = { QuotePagingSource(quoteAPI) }
    ).liveData

     fun observeNewsListPaginated(): Pager<Int, Product> {
        return Pager(
            config = PagingConfig(20, enablePlaceholders = true),
            remoteMediator = QuoteRemoteMediator(quoteAPI, quoteDataBase)
        ) {
            quoteDataBase.quoteDao().getQuotes()
        }

    }
}