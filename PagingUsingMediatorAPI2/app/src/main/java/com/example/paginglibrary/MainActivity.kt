package com.example.paginglibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paginglibrary.paging.QuotePagingAdapter
import com.example.paginglibrary.viewModel.QuoteViewModel
import com.example.paginglibrary.paging.LoaderAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: QuotePagingAdapter
    private lateinit var quoteViewModel: QuoteViewModel

    @ExperimentalPagingApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.quoteList)
        adapter = QuotePagingAdapter()
        quoteViewModel = ViewModelProvider(this)[QuoteViewModel::class.java]

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)


     /*  this.lifecycleScope.launch{
            quoteViewModel.getPopularMovies().collectLatest {
                adapter.submitData(lifecycle,it)
                recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                    header = LoaderAdapter(),
                    footer = LoaderAdapter()
                )

            }
        }*/

        this.lifecycleScope.launch {
            quoteViewModel.pager.collectLatest {
                adapter.submitData(lifecycle, it)


            }
        }
        recyclerView.adapter = adapter























    }


}