package com.ucopdesoft.issuelogger.models

data class Complaint(
    val complaintId: String,
    val userId: String,
    val complaintTitle: String,
    val complaintDescription: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val upVote: Int,
    val downVote: Int,
    val status: String,
    val date: Long
)