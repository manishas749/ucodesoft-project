package com.example.notes_app.datamodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notes_Table")    //Table created
data class Notes (
    @PrimaryKey(autoGenerate = true)
    val id :Int,
    @ColumnInfo(name = "title") var title:String,
    @ColumnInfo(name = "description") var description:String
)