package com.example.listerpros.model.updatetaskstatus

data class Notes(
    val created: String,
    val modified: String,
    val notes: String,
    val notes_added_by: NotesAddedBy
)