package com.example.newsappusingpaging

import android.icu.text.CaseMap.Title
import android.media.Image
import com.google.gson.annotations.SerializedName
import io.reactivex.internal.schedulers.NewThreadScheduler

class Response (
    @SerializedName("articles") val news : List<News>

)
data class News(
    val title: String,
    @SerializedName("urlToImage") val image: String
)