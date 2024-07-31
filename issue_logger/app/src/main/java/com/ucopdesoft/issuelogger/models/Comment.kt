package com.ucopdesoft.issuelogger.models

data class Comment(
    val complaintId: String,
    val userId: String,
    val comment: String,
    val date: Long
)
