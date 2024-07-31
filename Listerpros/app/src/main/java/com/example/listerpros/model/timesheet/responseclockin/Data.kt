package com.example.listerpros.model.timesheet.responseclockin

data class Data(
    val check_in_coordinate: CheckInCoordinate,
    val check_in_time: String,
    val check_out_coordinate: CheckOutCoordinate,
    val check_out_time: String,
    val created: String,
    val id: Int,
    val modified: String,
    val note: String
)