package com.example.paginglibrary.viewModel

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.paginglibrary.db.NewsRemoteMediator
import com.example.paginglibrary.db.QuoteDao
import com.example.paginglibrary.repository.QuoteRepository
import com.example.paginglibrary.retrofit.QuoteAPI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class QuoteViewModel @Inject constructor(private val quoteRepository: QuoteRepository,private val quoteDao: QuoteDao,private val quoteAPI: QuoteAPI):ViewModel() {

    val newsPaginated = quoteRepository.getAllNewsStream()
   // var response: MutableLiveData<Call<MoviesList>> = MutableLiveData()


    @ExperimentalPagingApi
    val pager = Pager(
        PagingConfig(pageSize = 10),
        remoteMediator = NewsRemoteMediator(quoteDao,quoteAPI,1)
    ) {
        quoteDao.getAllArticles()
    }.flow
}








