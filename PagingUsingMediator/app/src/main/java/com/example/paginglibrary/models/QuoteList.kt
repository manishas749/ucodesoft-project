package com.example.paginglibrary.models

data class QuoteList(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)