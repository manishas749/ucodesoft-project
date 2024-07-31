package com.example.fragmentsproject.repository

import androidx.lifecycle.LiveData
import com.example.fragmentsproject.dao.EmployeeDataDao
import com.example.fragmentsproject.data.EmployeeData

class EmployeeRepository(private val dao: EmployeeDataDao) {

    val readAllData:LiveData<List<EmployeeData>> = dao.readAllData()



    suspend fun addEmp(employeeData: EmployeeData)
    {
        dao.addEmployeeDetail(employeeData)
    }





}