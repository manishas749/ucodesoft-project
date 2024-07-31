package com.example.listerpros.dummydata

import com.example.listerpros.constants.Constants.Companion.ADD_NOTE_LIST1
import com.example.listerpros.constants.Constants.Companion.ADD_NOTE_LIST2
import com.example.listerpros.constants.Constants.Companion.ADD_NOTE_LIST3
import com.example.listerpros.constants.Constants.Companion.ADD_NOTE_LIST4
import com.example.listerpros.model.JobDetailData

// class for Dummy Data to be use in jobDetail fragment and MyJob fragment

class JobDetailsDummyData {

    var jobList = ArrayList<JobDetailData>()

    private var firstJob = JobDetailData("#2 Anytime 3HR Phtots Vacant- Keith Reece HomeSmart","1616 N Alta Mesa Dr, Mesa, AZ 85205","9017447805",
        "9017447805","On the way","December 07, 2021","Anytime","Agent","In-progress",
        "Vacant","0933 Alene Glens Apt. 485, New York",421,"Jordan","Professional Photos","Virtual Photos","HD photos Version","3 Hours Photo Delivery", NoteList = ADD_NOTE_LIST1 )

    private var secondJob = JobDetailData("#1 ANYTIME photos Vacant - Robert Foreman Home & Away Realty","1616 N Alta Mesa Dr, Mesa, AZ 85205","9017447805",
        "9017447805","On the way","December 07, 2021","Anytime","Agent","In-progress",
        "Vacant","0933 Alene Glens Apt. 485, New York",421,"Peter","Professional Photos","Virtual Photos","HD photos Version","3 Hours Photo Delivery", NoteList = ADD_NOTE_LIST2)

    private var thirdJob = JobDetailData("1 SCHEDULED Photos Vacant - Jacqui Firestone Russ Lyon Sotheby International Realty ","7700 E Gainey Ranch Rd, Scottsdale, AZ 85258","9017447805",
        "9017447805","On the way","December 07, 2021","Anytime","Agent","New",
        "Vacant","0933 Alene Glens Apt. 485, New York",421,"Andrew","Professional Photos","Virtual Photos","HD photos Version","3 Hours Photo Delivery", NoteList = ADD_NOTE_LIST3)

    private var forthJob = JobDetailData("1 SCHEDULED Photos Vacant - Jacqui Firestone Russ Lyon Sotheby International Realty ","7700 E Gainey Ranch Rd, Scottsdale, AZ 85258","9017447805",
        "9017447805","On the way","December 07, 2021","Anytime","Agent","New",
        "Vacant","0933 Alene Glens Apt. 485, New York",421,"Mia","Professional Photos","Virtual Photos","HD photos Version","3 Hours Photo Delivery", NoteList = ADD_NOTE_LIST4)

     fun jobList(){
         jobList.clear()
         jobList.add(firstJob)
         jobList.add(secondJob)
         jobList.add(thirdJob)
         jobList.add(forthJob)

     }

}