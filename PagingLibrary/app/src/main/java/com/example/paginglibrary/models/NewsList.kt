package com.example.paginglibrary.models

import com.example.paginglibrary.models.Article

data class NewsList(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)