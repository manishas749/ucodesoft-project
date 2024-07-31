package com.example.listerpros.model.timesheet.gettimesheet

data class ResponseTimeSheet(
    val `data`: List<TimeSheetData>,
    val status: Int
)