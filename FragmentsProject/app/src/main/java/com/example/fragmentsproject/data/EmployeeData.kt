package com.example.fragmentsproject.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Emp_Table")
data class EmployeeData(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo(name = "firstName") var firstName:String,
    @ColumnInfo(name = "lastName") var lastName: String,
    @ColumnInfo(name = "userName") var userName:String,
    @ColumnInfo(name = "dateOfBirth") var dateOfBirth:String,
    @ColumnInfo(name = "emailId") var emailId:String,
    @ColumnInfo(name = "password") var password:String,

)

