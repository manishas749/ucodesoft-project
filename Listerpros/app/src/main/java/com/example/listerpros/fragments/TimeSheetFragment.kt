package com.example.listerpros.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.listerpros.R
import com.example.listerpros.adapter.TimesheetAdapter
import com.example.listerpros.databinding.FragmentTimeSheetsBinding
import com.example.listerpros.model.timesheet.gettimesheet.TimeSheetData
import com.example.listerpros.model.timesheet.responseclockin.ResponseClockIn
import com.example.listerpros.model.timesheet.gettimesheet.ResponseTimeSheet
import com.example.listerpros.model.timesheet.responseclockout.ResponseClockOut
import com.example.listerpros.preferences.TimeSheetTokenManager
import com.example.listerpros.utils.Helper
import com.example.listerpros.utils.NetworkCheck
import com.example.listerpros.utils.ProgressBarDialog
import com.example.listerpros.utils.WeeklyCalendar
import com.example.listerpros.viewmodel.TimeSheetViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class TimeSheetFragment : Fragment() {

    private lateinit var binding: FragmentTimeSheetsBinding
    private lateinit var tokenManager: TimeSheetTokenManager
    private lateinit var timesheetAdapter: TimesheetAdapter
    private lateinit var networkCheck: NetworkCheck
    private lateinit var progressBarDialog: ProgressBarDialog
    private lateinit var viewModel: TimeSheetViewModel
    private lateinit var weeklyCalendar: WeeklyCalendar
    private val timer = Timer()
    private var timeSheetStartDate: String = ""
    private var timesheetEndDate: String = ""
    private var currentLocation: Location? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val requestCode = 101
    private var addNote: String = ""
    private var currentDate: String = ""
    var longitude: Double = 12.55
    var latitude: Double = 12.55
    var list: List<TimeSheetData>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentTimeSheetsBinding.inflate(inflater, container, false)
        tokenManager = TimeSheetTokenManager(requireContext())
        networkCheck = NetworkCheck(requireContext())

        viewModel = ViewModelProvider(this)[TimeSheetViewModel::class.java]
        progressBarDialog = ProgressBarDialog(requireContext())
        list = listOf()
        weeklyCalendar = WeeklyCalendar()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBarDialog.showProgressBar()

        setUpFormatTimeSheet()    //to pass start and end time to ViewModel

        getTimeSheetObserver()    //observer to get timesheet date

        getLongitudeLatitude()

        binding.startButton.setOnClickListener()
        {
            startStopAction() //Start Stop action of Button to start time and stop

        }
        if (tokenManager.timeCounting()) {

            startTimer()


        } else {
            stopTimer()
            if (tokenManager.startTime() != null && tokenManager.stopTime() != null) {
                val time = Date().time - calcRestartTime().time
                binding.totalTimeDisplay.text = timeStringFromLong(time)
            }
        }
        timer.scheduleAtFixedRate(TimeTask(), 0, 500)
        getCurrentDate()        //to fetch current date and display

    }

    private fun getCurrentDate() {
        val list = Helper.getCurrentDate()
        binding.date.text = list[0]
        tokenManager.saveCurrentDate(currentDate)
        binding.weekDay.text = list[1]
        binding.year.text = list[2]
    }

    // to show running time in TextBox
    private inner class TimeTask : TimerTask() {

        override fun run() {
            MainScope().launch {
                if (tokenManager.timeCounting()) {
                    val time = Date().time - tokenManager.startTime()!!.time

                    binding.totalTimeDisplay.text =
                        timeStringFromLong(time)   //to fetch difference in time between start time
                }
            }
        }
    }

    private fun getLongitudeLatitude() {

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireContext() as Activity,
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                requestCode
            )

        }
        val task = fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                latitude = currentLocation!!.latitude
                longitude = currentLocation!!.longitude
            }
        }
    }

    private fun setTimeSheetList(list: List<TimeSheetData>?) {
        try {
            val linearLayout = LinearLayoutManager(context)
            binding.timesheetRecycle.layoutManager = linearLayout
            timesheetAdapter = TimesheetAdapter(requireContext(), list!!)
            binding.timesheetRecycle.adapter = timesheetAdapter
            progressBarDialog.hideProgressBar()
        } catch (ex: Exception) {
            Log.d("exception", ex.toString())
        }

    }

    private fun startStopAction() {
        val networkCheck = networkCheck.checkNetwork()
        if (networkCheck) {
            if (tokenManager.timeCounting()) {

                openDialogForNotes() //Dialog for Notes

            } else {
                val date = Date()

                if (tokenManager.stopTime() != null) {
                    tokenManager.setStartTime(calcRestartTime())
                    tokenManager.setStopTime(null)
                } else {
                    tokenManager.setStartTime(date)

                }
                progressBarDialog.showProgressBar()
                val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                val startTimePost = formatter.format(date)
                viewModel.clockIn(
                    "I",
                    timeSheetStartDate,
                    timesheetEndDate,
                    startTimePost,
                    latitude,
                    longitude
                )

                if (!viewModel.responseClockIn.hasObservers()) {
                    postStartTimeSheetObserver()
                }
            }
        } else {
            Toast.makeText(
                context,
                resources.getString(R.string.noInternet),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun endTimePostInClockOut() {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        val date = Date()
        val endTimePost = formatter.format(date)
        tokenManager.setStopTime(date)
        viewModel.clockOut(
            "O", timeSheetStartDate, timesheetEndDate, endTimePost,
            addNote,
            latitude,
            longitude
        )

        if (!viewModel.responseClockOut.hasActiveObservers()) {
            postEndTimeSheetObserver()   //observer for end timer
        }
    }


    private fun calcRestartTime(): Date {
        val difference = tokenManager.startTime()!!.time - tokenManager.stopTime()!!.time
        return Date(System.currentTimeMillis() + difference)
    }

    // conversion from milliseconds to seconds minutes and hours

    private fun timeStringFromLong(ms: Long): String {
        val seconds = (ms / 1000) % 60
        val minutes = (ms / (1000 * 60) % 60)
        val hours = (ms / (1000 * 60 * 60) % 24)
        val hoursFormat = "" + hours + "h"
        val minutesFormat = "" + minutes + "m"
        val secondsFormat = "" + seconds + "s"
        return if (hours > 0) {
            ("$hoursFormat $minutesFormat $secondsFormat")
        } else {
            (" $minutesFormat $secondsFormat")
        }
    }

    //change text to start and image if stop is clicked
    private fun stopTimer() {
        tokenManager.setTimerCounting(false)
        binding.startTimer.text = resources.getString(R.string.startTimer)
        binding.play.setImageResource(R.drawable.timesheet_start_timer_icon)
    }

    private fun openDialogForNotes() {
        val builder = AlertDialog.Builder(context)
        val view: View = layoutInflater.inflate(R.layout.dialog_notes, null)

        val addNotes = view.findViewById<EditText>(R.id.addNotesTimeSheet)
        builder.setView(view)
        val alertDialog: AlertDialog = builder.create()
        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        alertDialog.show()
        alertDialog.setCanceledOnTouchOutside(false)
        val doneButton = view.findViewById<TextView>(R.id.doneTimeSheet)
        doneButton.setOnClickListener()
        {

            addNote = addNotes.text.toString()
            progressBarDialog.showProgressBar()
            endTimePostInClockOut()   // to fetch end time and post in observer
            alertDialog.dismiss()
        }
    }

    //change text to stop and image if start is clicked
    private fun startTimer() {
        tokenManager.setTimerCounting(true)
        binding.startTimer.text = resources.getString(R.string.StopTimer)
        binding.play.setImageResource(R.drawable.ic_baseline_stop_24)

    }


    private fun postStartTimeSheetObserver() {
        try {
            viewModel.responseClockIn.observe(viewLifecycleOwner, Observer { call ->
                call?.clone()?.enqueue(object : retrofit2.Callback<ResponseClockIn> {
                    override fun onResponse(
                        call: Call<ResponseClockIn>,
                        response: Response<ResponseClockIn>,
                    ) {
                        if (response.isSuccessful) {
                            val list = response.body()?.data!!
                            setTimeSheetList(list)
                            startTimer()
                            progressBarDialog.hideProgressBar()

                        } else {
                            Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show()
                            progressBarDialog.hideProgressBar()
                        }

                    }

                    override fun onFailure(call: Call<ResponseClockIn>, t: Throwable) {
                        Toast.makeText(
                            context,
                            resources.getString(R.string.networkFailed),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBarDialog.hideProgressBar()
                    }
                })
            })
        } catch (e: Exception) {
            Log.d("exception", e.toString())
        }
    }


    private fun setUpFormatTimeSheet() {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = format.format(Date())
        timeSheetStartDate = "" + currentDate + "T00:00:00Z"
        timesheetEndDate = "" + currentDate + "T23:59:59Z"
        viewModel.getTimeSheet(timeSheetStartDate, timesheetEndDate)
    }

    private fun getTimeSheetObserver() {
        viewModel.responseTimeSheet.observe(viewLifecycleOwner, Observer { call ->
            call?.enqueue(object : retrofit2.Callback<ResponseTimeSheet> {
                override fun onResponse(
                    call: Call<ResponseTimeSheet>,
                    response: Response<ResponseTimeSheet>,
                ) {
                    if (response.isSuccessful) {

                        list = response.body()?.data
                        checkIsListEmpty(list)
                        setTimeSheetList(list)
                        Log.d("List", list?.size.toString())
                        //binding.totalTimeDisplay.text=totalTimeCalculator()

                    } else {
                        Toast.makeText(
                            context,
                            resources.getString(R.string.tryAgain),
                            Toast.LENGTH_SHORT

                        ).show()
                        progressBarDialog.hideProgressBar()
                    }
                }

                override fun onFailure(call: Call<ResponseTimeSheet>, t: Throwable) {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.networkFailed),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressBarDialog.hideProgressBar()
                }
            })
        })
    }

    private fun postEndTimeSheetObserver() {
        try {
            viewModel.responseClockOut.observe(viewLifecycleOwner, Observer { call ->
                call?.clone()?.enqueue(object : retrofit2.Callback<ResponseClockOut> {
                    override fun onResponse(
                        call: Call<ResponseClockOut>,
                        response: Response<ResponseClockOut>,
                    ) {
                        if (response.isSuccessful) {
                            val list = response.body()?.data!!
                            setTimeSheetList(list)
                            stopTimer()
                            progressBarDialog.hideProgressBar()

                        } else {
                            Toast.makeText(
                                context,
                                resources.getString(R.string.tryAgain),
                                Toast.LENGTH_SHORT
                            ).show()
                            progressBarDialog.hideProgressBar()
                        }
                    }

                    override fun onFailure(call: Call<ResponseClockOut>, t: Throwable) {
                        Toast.makeText(
                            context,
                            resources.getString(R.string.networkFailed),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBarDialog.hideProgressBar()
                    }
                })
            })

        } catch (e: Exception) {
            Log.d("exception", e.toString())
        }
    }

    private fun checkIsListEmpty(list: List<TimeSheetData>?) {
        if (list!!.size <= 0) {
            binding.totalTimeDisplay.text = timeStringFromLong(0)
            tokenManager.countingTimeClear()
            tokenManager.startTimeClear()
            tokenManager.stopTimeClear()
        }
    }

    fun totalTimeCalculator(): String {
        var totalTime: Long = 0
        for (i in 0 until list!!.size) {
            try {
                val startTime = list!![i].check_in_time.subSequence(11, 19)
                val endTime = list!![i].check_out_time.subSequence(11, 19)
                val timeDifference =
                    Helper.timeDiffernce(startTime.toString(), endTime.toString()).second
                totalTime += timeDifference
            } catch (e: Exception) {
                Log.d("ex", e.toString())
            }
        }
        return Helper.hourMinuteCalculation(totalTime)
    }
}