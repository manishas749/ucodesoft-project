package com.ucopdesoft.issuelogger.models

data class News(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)