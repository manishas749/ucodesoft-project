package com.example.listerpros.utils

import java.util.*

class WeeklyCalendar {

    val lastDayInCalendar: Calendar? = Calendar.getInstance(Locale.ENGLISH)

    //private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal: Calendar? = Calendar.getInstance(Locale.ENGLISH)

    // current date
    val currentDate: Calendar = Calendar.getInstance(Locale.ENGLISH)
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]
    private val currentWeek = currentDate[Calendar.WEEK_OF_MONTH]
    private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]

    // selected date
    var selectedDay: Int = currentDay
    private var selectedWeek: Int = currentWeek
    var selectedMonth: Int = currentMonth
    var selectedYear: Int = currentYear
    val dates = ArrayList<Date>()

    var changeWeekCalendar: Calendar? = Calendar.getInstance(Locale.ENGLISH)

    //changeWeek if next or previous week is not the week month
    fun setUpCalendar(changeWeek: Calendar? = null) {
        val monthCalendar = cal?.clone() as Calendar
        //val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val maxDaysInWeek = cal.getActualMaximum(Calendar.DAY_OF_WEEK)

        // If changeWeek is not null, then We will take the day, month, and year from it,
        //otherwise set the selected date as the current date.
        selectedDay =
            when {
                changeWeek != null -> changeWeek.getActualMinimum(Calendar.DAY_OF_MONTH)
                else -> currentDay
            }
        selectedWeek =
            when {
                changeWeek != null -> changeWeek.getActualMinimum(Calendar.WEEK_OF_MONTH)
                else -> currentWeek
            }
        selectedMonth =
            when {
                changeWeek != null -> changeWeek[Calendar.MONTH]
                else -> currentMonth
            }
        selectedYear =
            when {
                changeWeek != null -> changeWeek[Calendar.YEAR]
                else -> currentYear
            }
        changeWeekCalendar = changeWeek
        var currentPosition = 0
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_WEEK, 1)

        // Fill dates with days and set currentPosition.
        while (dates.size < maxDaysInWeek) {
            // get position of selected day
            if (monthCalendar[Calendar.DAY_OF_WEEK] == selectedWeek)
                currentPosition = dates.size
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_WEEK, 1)
        }

    }

    fun nextWeekCalendar() {
        cal!!.add(Calendar.WEEK_OF_MONTH, -1)
        setUpCalendar(changeWeek = cal)
    }

    fun previousWeekCalendar() {
        if (cal!!.before(lastDayInCalendar)) {
            cal.add(Calendar.WEEK_OF_MONTH, 1)
            setUpCalendar(changeWeek = cal)
        }
    }
}