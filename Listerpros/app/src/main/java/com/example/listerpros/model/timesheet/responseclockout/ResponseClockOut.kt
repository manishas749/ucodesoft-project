package com.example.listerpros.model.timesheet.responseclockout

import com.example.listerpros.model.timesheet.gettimesheet.TimeSheetData

data class ResponseClockOut(
    val `data`: List<TimeSheetData>
)