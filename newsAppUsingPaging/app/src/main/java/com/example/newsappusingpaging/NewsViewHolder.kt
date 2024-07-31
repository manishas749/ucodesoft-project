package com.example.newsappusingpaging

import androidx.recyclerview.widget.RecyclerView
import com.example.newsappusingpaging.databinding.ItemNewsBinding
import com.squareup.picasso.Picasso

class NewsViewHolder(private val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(news: News?) {
        if (news != null) {
            binding.txtNewsName.text = news.title
            Picasso.get().load(news.image).into(binding.imgNewsBanner)
        }
    }
}
