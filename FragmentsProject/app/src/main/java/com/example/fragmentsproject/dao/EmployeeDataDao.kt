package com.example.fragmentsproject.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fragmentsproject.data.EmployeeData


@Dao
interface EmployeeDataDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addEmployeeDetail(employeeData: EmployeeData)


    @Query("SELECT * FROM emp_table ORDER BY id ASC")
    fun readAllData():LiveData<List<EmployeeData>>







}