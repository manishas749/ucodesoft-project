package com.example.listerpros.constants

import com.example.listerpros.model.AddNotes
import com.example.listerpros.model.getmyprofile.MyProfile
import com.example.listerpros.model.timesheet.gettimesheet.TimeSheetData

class Constants {
    companion object {
        const val PREFERENCES = "com.listerpros.preferences"
        const val BASE_URL = "https://demo.listerpros.com/api/"
        var START_TIME_KEY = "startKey"
        const val STOP_TIME_KEY = "stopKey"
        const val COUNTING_KEY = "countingKey"
        const val JOB_LIST = "jobList"
        const val START_TIME = "00:00"
        const val TOKEN = "token"
        var BEARER_TOKEN = ""
        var count = 0
        var DIFFERENCETOTAL:Long =0
        var differenceInMilliSeconds:Long=0
        var JOB_TASK_STATUS_COUNT = 0
        const val CURRENT_DATE = ""
        var ADD_NOTE_LIST1: ArrayList<AddNotes> = ArrayList()
        var ADD_NOTE_LIST2: ArrayList<AddNotes> = ArrayList()
        var ADD_NOTE_LIST3: ArrayList<AddNotes> = ArrayList()
        var ADD_NOTE_LIST4: ArrayList<AddNotes> = ArrayList()
        var PROFILE_DETAIL: ArrayList<MyProfile>? = ArrayList()
        var JOB_ID = 0
        var NOTE_ID = 0
        var LONG=0.0
    }

}