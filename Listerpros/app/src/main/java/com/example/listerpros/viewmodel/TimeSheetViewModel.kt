package com.example.listerpros.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.listerpros.model.timesheet.responseclockin.ResponseClockIn
import com.example.listerpros.model.timesheet.gettimesheet.ResponseTimeSheet
import com.example.listerpros.model.timesheet.responseclockout.ResponseClockOut
import com.example.listerpros.repositiory.TimeSheetRepository
import kotlinx.coroutines.launch
import retrofit2.Call

class TimeSheetViewModel(application: Application) : AndroidViewModel(application) {

    val responseClockIn: MutableLiveData<Call<ResponseClockIn>> = MutableLiveData()
    val responseTimeSheet: MutableLiveData<Call<ResponseTimeSheet>> = MutableLiveData()
    val responseClockOut: MutableLiveData<Call<ResponseClockOut>> = MutableLiveData()
    private val timeSheetRepository: TimeSheetRepository = TimeSheetRepository()

    fun clockIn(
        status: String,
        start_date: String,
        end_date: String,
        clock_in: String,
        latitude: Double,
        longitude: Double,
    ) {
        viewModelScope.launch {
            val response =
                timeSheetRepository.clockIn(status, start_date,end_date, clock_in, latitude, longitude)
            responseClockIn.value = response
        }
    }

    fun clockOut(
        status: String,
        start_date: String,
        end_date: String,
        clock_out: String,
        note: String,
        latitude: Double,
        longitude: Double,
    ) {
        viewModelScope.launch {
            val response = timeSheetRepository.clockOut(status,
                start_date,
                end_date,
                clock_out,
                note,
                latitude,
                longitude)
            responseClockOut.value = response
        }
    }

    fun getTimeSheet(start_date: String, end_date: String) {
        viewModelScope.launch {
            val response = timeSheetRepository.getTimeSheet(start_date, end_date)
            responseTimeSheet.value = response
        }
    }
}