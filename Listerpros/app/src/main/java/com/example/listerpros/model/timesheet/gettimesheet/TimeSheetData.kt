package com.example.listerpros.model.timesheet.gettimesheet

data class TimeSheetData(
    val check_in_coordinate: CheckInCoordinateTimeSheet,
    val check_in_time: String,
    val check_out_coordinate: CheckOutCoordinateTimeSheet,
    val check_out_time: String,
    val created: String,
    val id: Int,
    val modified: String,
    val note: String
)