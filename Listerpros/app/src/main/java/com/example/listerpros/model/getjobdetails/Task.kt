package com.example.listerpros.model.getjobdetails

data class Task(
    val applicable_for: Int,
    val id: Int,
    val name: String,
    val notes: Notes,
    val status: String
)