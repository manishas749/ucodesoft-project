package com.example.listerpros.model.addnotesmodified

data class Note(
    val created: String,
    val id: Int,
    val modified: String,
    val note_added_by: NoteAddedBy,
    val notes: String,
    val order_id: Int
)