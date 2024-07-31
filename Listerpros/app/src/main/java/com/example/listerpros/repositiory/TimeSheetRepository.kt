package com.example.listerpros.repositiory

import com.example.listerpros.api.RetrofitInstance
import com.example.listerpros.model.timesheet.responseclockin.ResponseClockIn
import com.example.listerpros.model.timesheet.gettimesheet.ResponseTimeSheet
import com.example.listerpros.model.timesheet.responseclockout.ResponseClockOut
import retrofit2.Call

class TimeSheetRepository {

    fun clockIn(status: String,start_date: String,end_date: String,clock_in:String,latitude:Double,longitude:Double): Call<ResponseClockIn> {
        return RetrofitInstance.api.clockIn(status,start_date,end_date,clock_in,latitude,longitude)
    }

    fun getTimeSheet(start_date:String,end_date:String): Call<ResponseTimeSheet> {
        return RetrofitInstance.api.getTimesheet(start_date,end_date)
    }

    fun clockOut(status: String,start_date: String,end_date: String,clock_out:String,note:String,latitude:Double,longitude:Double): Call<ResponseClockOut> {
        return RetrofitInstance.api.clockOut(status,start_date,end_date,clock_out, note,latitude,longitude)
    }

}