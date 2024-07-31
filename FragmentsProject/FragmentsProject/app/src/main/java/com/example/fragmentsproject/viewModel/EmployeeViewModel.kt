package com.example.fragmentsproject.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.fragmentsproject.data.EmployeeData
import com.example.fragmentsproject.database.EmployeeDetailDatabase
import com.example.fragmentsproject.repository.EmployeeRepository
import kotlinx.coroutines.launch

class EmployeeViewModel(private val application: Application):AndroidViewModel(application)
{
    private val repository:EmployeeRepository
    val readAllData:LiveData<List<EmployeeData>>

    init {
        val dao = EmployeeDetailDatabase.getDatabase(application).EmployeeDataDao()
        repository = EmployeeRepository(dao)
        readAllData= repository.readAllData
    }

    fun addEmp(employeeData: EmployeeData)
    {
        viewModelScope.launch {
            repository.addEmp(employeeData)
        }
    }

}