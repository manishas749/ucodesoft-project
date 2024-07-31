package com.example.paginglibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paginglibrary.paging.QuotePagingAdapter
import com.example.paginglibrary.viewModel.QuoteViewModel
import com.example.paginglibrary.paging.LoaderAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: QuotePagingAdapter
    lateinit var quoteViewModel: QuoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.quoteList)
        adapter = QuotePagingAdapter()
        quoteViewModel = ViewModelProvider(this)[QuoteViewModel::class.java]

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        quoteViewModel.list.observe(this,Observer{


            adapter.submitData(lifecycle,it)
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LoaderAdapter(),
                footer = LoaderAdapter()
            )

        })

    }
}