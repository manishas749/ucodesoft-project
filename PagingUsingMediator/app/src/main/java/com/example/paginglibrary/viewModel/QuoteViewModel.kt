package com.example.paginglibrary.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.ExperimentalPagingApi
import com.example.paginglibrary.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class QuoteViewModel @Inject constructor(quoteRepository: QuoteRepository):ViewModel() {

    val newsPaginated = quoteRepository.observeNewsListPaginated().flow.asLiveData()



}