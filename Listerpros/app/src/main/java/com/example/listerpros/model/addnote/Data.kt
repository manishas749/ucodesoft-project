package com.example.listerpros.model.addnote

data class Data(
    val additional_info: String,
    val end: String,
    val id: Int,
    val location: String,
    val message: String,
    val notes: List<Note>,
    val start: String,
    val status: String,
    val summary: String,
    val title: String
)