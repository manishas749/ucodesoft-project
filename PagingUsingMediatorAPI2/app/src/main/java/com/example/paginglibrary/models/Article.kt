package com.example.paginglibrary.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Quote")
data class Article(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: Source?,
    @PrimaryKey(autoGenerate = false)
    val title: String,
    val url: String,
    val urlToImage: String
):Parcelable