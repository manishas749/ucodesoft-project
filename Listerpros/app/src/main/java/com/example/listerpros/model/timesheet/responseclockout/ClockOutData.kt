package com.example.listerpros.model.timesheet.responseclockout

data class ClockOutData(
    val check_in_coordinate: CheckInCoordinateClockOut,
    val check_in_time: String,
    val check_out_coordinate: CheckOutCoordinateClockOut,
    val check_out_time: String,
    val created: String,
    val id: Int,
    val modified: String,
    val note: String
)