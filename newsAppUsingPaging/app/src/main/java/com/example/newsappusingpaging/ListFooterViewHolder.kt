package com.example.newsappusingpaging

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsappusingpaging.databinding.ItemListFooterBinding

class ListFooterViewHolder(private val binding: ItemListFooterBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(status: State?) {
        binding.progressBar.visibility = if (status == State.LOADING) VISIBLE else View.INVISIBLE
        binding.txtError.visibility = if (status == State.ERROR) VISIBLE else View.INVISIBLE
    }




}