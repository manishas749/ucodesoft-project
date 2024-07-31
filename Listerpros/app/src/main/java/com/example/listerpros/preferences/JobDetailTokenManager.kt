package com.example.listerpros.preferences
import android.content.Context
import com.example.listerpros.constants.Constants.Companion.JOB_LIST
import com.example.listerpros.dummydata.JobDetailsDummyData
import com.example.listerpros.model.JobDetailData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class JobDetailTokenManager(context: Context)
{
    private val timeSheet=TimeSheetTokenManager(context)

  //  private val myJobsFragment = MyJobsFragment()
    private val jobList = JobDetailsDummyData()


    fun saveJobs(List:ArrayList<JobDetailData>)   // to save the jobs in recycle view
    {
        val editor= timeSheet.prefs.edit()
        val gson = Gson()
        val json = gson.toJson(List)
        editor.putString(JOB_LIST,json)
        editor.apply()
    }

    fun loadList()    // to get jobs in recyclerview
    {
        val gson= Gson()
        val json: String? = timeSheet.prefs.getString(JOB_LIST, null)
        val type: Type = object : TypeToken<ArrayList<JobDetailData>>() {}.type
        if(json==null)
        {
            jobList.jobList= ArrayList()
        }
        if(json!=null)
        {
           jobList.jobList = gson.fromJson(json, type)
        }
    }

}