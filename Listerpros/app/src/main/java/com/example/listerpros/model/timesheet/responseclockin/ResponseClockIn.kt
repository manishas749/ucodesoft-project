package com.example.listerpros.model.timesheet.responseclockin

import com.example.listerpros.model.timesheet.gettimesheet.TimeSheetData

data class ResponseClockIn(
    val `data`: List<TimeSheetData>
)