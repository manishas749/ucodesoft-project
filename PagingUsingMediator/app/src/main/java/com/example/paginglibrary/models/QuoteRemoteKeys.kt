package com.example.paginglibrary.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class QuoteRemoteKeys (
    @PrimaryKey(autoGenerate = false)
    val id:Int,
    val prevPage:Int?,
    val nextPage: Int?

)