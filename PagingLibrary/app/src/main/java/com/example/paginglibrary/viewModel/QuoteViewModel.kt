package com.example.paginglibrary.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.paginglibrary.repository.QuoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class QuoteViewModel @Inject constructor(private val quoteRepository: QuoteRepository):ViewModel() {

    val list = quoteRepository.getQuotes().cachedIn(viewModelScope)



}