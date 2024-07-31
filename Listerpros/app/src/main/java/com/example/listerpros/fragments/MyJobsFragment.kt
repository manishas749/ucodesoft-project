package com.example.listerpros.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.listerpros.R
import com.example.listerpros.adapter.CalendarAdapter
import com.example.listerpros.adapter.JobDetailsAdapter
import com.example.listerpros.databinding.FragmentMyJobsBinding
import com.example.listerpros.dummydata.JobDetailsDummyData
import com.example.listerpros.model.JobDetailData
import com.example.listerpros.model.getmyjobs.Data
import com.example.listerpros.model.getmyjobs.GetMyJobs
import com.example.listerpros.preferences.JobDetailTokenManager
import com.example.listerpros.utils.Helper
import com.example.listerpros.utils.NetworkCheck
import com.example.listerpros.utils.ProgressBarDialog
import com.example.listerpros.utils.WeeklyCalendar
import com.example.listerpros.viewmodel.GetMyJobsViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class MyJobsFragment : Fragment() {

    private lateinit var binding: FragmentMyJobsBinding
    private lateinit var viewModel: GetMyJobsViewModel
    private lateinit var tokenManager: JobDetailTokenManager
    private lateinit var weekDateRecyclerView: RecyclerView
    private lateinit var jobDetailRecyclerView: RecyclerView
    private lateinit var progressDialog: ProgressBarDialog
    private lateinit var networkCheck: NetworkCheck
    private lateinit var jobDetailsList: ArrayList<JobDetailData>
    lateinit var weeklyCalendar: WeeklyCalendar
    private val jobList = JobDetailsDummyData()
    lateinit var filterJobList: List<Data>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyJobsBinding.inflate(inflater, container, false)
        progressDialog = context?.let { ProgressBarDialog(it) }!!
        networkCheck = NetworkCheck(requireContext())
        tokenManager = JobDetailTokenManager(requireContext())
        weeklyCalendar = WeeklyCalendar()
        viewModel = ViewModelProvider(this)[GetMyJobsViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterJobList = mutableListOf()
        progressDialog.showProgressBar()

        tokenManager.loadList()    // to get data from shared preference and load on fragment
        jobDetailsList = ArrayList()
        jobList.jobList()
        tokenManager.saveJobs(jobList.jobList)        // to save data in shared preference
        weekDateRecyclerView = binding.dateRecycleView
        jobDetailRecyclerView = binding.jobDetailsRecycleView

        getCurrentDate()         // to get current date

        //This is the maximum month that the calendar will display.
        weeklyCalendar.lastDayInCalendar?.add(Calendar.WEEK_OF_MONTH, 12)
        weeklyCalendar.setUpCalendar()

        //  Go to the previous week
        binding.prevArrow.setOnClickListener {
            weeklyCalendarChangeListener(1)
        }

        //  Go to the next week
        binding.nextArrow.setOnClickListener {
            weeklyCalendarChangeListener(0)
        }

        setUpWeekCalenderRecycleView(
            weeklyCalendar.dates,
            weeklyCalendar.currentDate,
            weeklyCalendar.changeWeekCalendar
        )

        getMyJobs()
        getMyJobsObserver()
    }

    private fun weeklyCalendarChangeListener(value: Int) {
        if (networkCheck.checkNetwork()) {
            progressDialog.showProgressBar()

            if (value == 1) {
                weeklyCalendar.nextWeekCalendar()
            } else {
                weeklyCalendar.previousWeekCalendar()
            }
            getMyJobs()
           // setDateWithNextPreviousArrow(weeklyCalendar.dates)
        } else {
            Toast.makeText(context, resources.getString(R.string.noInternet), Toast.LENGTH_SHORT)
                .show()
        }
    }

    // to get current date
    private fun getCurrentDate() {
        val list = Helper.getCurrentDate()
        binding.date.text = list[0]
        binding.weekDay.text = list[1]
        binding.year.text = list[2]
    }

    // to set calender above
    private fun setUpWeekCalenderRecycleView(
        dates: ArrayList<Date>,
        currentDate: Calendar,
        changeWeek: Calendar?,
    ) {
        // Assigning calendar view.
           try {
               val gridLayoutManager = GridLayoutManager(context, 7)
               weekDateRecyclerView.layoutManager = gridLayoutManager
           }catch (e: Exception){
               Log.d("expception", e.toString())
           }

            val calendarAdapter = CalendarAdapter(requireContext(), dates, currentDate, changeWeek)
            weekDateRecyclerView.adapter = calendarAdapter


        //After calling up the OnClickListener, then specific item layout color changed .
        calendarAdapter.setOnItemClickListener(object : CalendarAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {

                val clickCalendar = Calendar.getInstance()
                clickCalendar.time = dates[position]

                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val currentJobDate = dateFormat.format(clickCalendar.time)
                jobFilterByDate(currentJobDate.toString())

                weeklyCalendar.selectedDay = clickCalendar[Calendar.DAY_OF_MONTH]
                weeklyCalendar.selectedMonth = clickCalendar[Calendar.MONTH] + 1
                weeklyCalendar.selectedYear = clickCalendar[Calendar.YEAR]

                val setDateFormat = SimpleDateFormat("dd:EEEE:LLL,yyyy", Locale.US)
                val setJobDate = setDateFormat.format(clickCalendar.time)

                val setDateList: List<String> = setJobDate.toString().split(":")

                binding.date.text = setDateList[0]
                binding.weekDay.text = setDateList[1]
                binding.year.text = setDateList[2]

            }
        })
    }

    // Set list into RecycleView
    private fun setUpJobDetailList(list: List<Data>?) {
        val linearLayout = LinearLayoutManager(context)
        jobDetailRecyclerView.layoutManager = linearLayout
        val jobDetailsAdapter = list?.let { JobDetailsAdapter(it) }
        jobDetailRecyclerView.adapter = jobDetailsAdapter

    }

    private fun getMyJobs() {
        val startDate = weeklyCalendar.dates[0]
        val endDate = weeklyCalendar.dates[weeklyCalendar.dates.size - 1]
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val startDateFormat = dateFormatter.format(startDate)
        val endDateFormat = dateFormatter.format(endDate)
        val jobsStartDate = "" + startDateFormat + "T00:00:00Z"
        val jobsEndDate = "" + endDateFormat + "T23:59:59Z"

        viewModel.getJobs(jobsStartDate, jobsEndDate)

    }

    fun setDateWithNextPreviousArrow(dates: ArrayList<Date>){
        val setDateFormat = SimpleDateFormat("dd:EEEE:LLL,yyyy", Locale.US)
        val setJobDate = setDateFormat.format(dates[0])

        val setDateList: List<String> = setJobDate.toString().split(":")

        binding.date.text = setDateList[0]
        binding.weekDay.text = setDateList[1]
        binding.year.text = setDateList[2]
    }

    private fun getMyJobsObserver() {
        viewModel.responseGetMyJobs.observe(viewLifecycleOwner) { call ->
            call?.enqueue(object : Callback<GetMyJobs> {
                override fun onResponse(call: Call<GetMyJobs>, response: Response<GetMyJobs>) {

                    if (response.isSuccessful) {
                        setUpWeekCalenderRecycleView(
                            weeklyCalendar.dates,
                            weeklyCalendar.currentDate,
                            weeklyCalendar.changeWeekCalendar
                        )


                        val getMyJobsList = response.body()?.data
                        filterJobList = response.body()?.data!!
                        if (getMyJobsList != null) {
                            if (getMyJobsList.isNotEmpty()) {

                                jobDetailRecyclerView.visibility = View.VISIBLE
                                binding.noJobAssign.visibility = View.GONE
                                setUpJobDetailList(getMyJobsList)
                            } else {
                                jobDetailRecyclerView.visibility = View.GONE
                                binding.noJobAssign.visibility = View.VISIBLE
                            }
                        }
                        progressDialog.hideProgressBar()

                    } else {
                        //   Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show()
                        progressDialog.hideProgressBar()
                        setUpWeekCalenderRecycleView(
                            weeklyCalendar.dates,
                            weeklyCalendar.currentDate,
                            weeklyCalendar.changeWeekCalendar
                        )
                    }
                }

                override fun onFailure(call: Call<GetMyJobs>, t: Throwable) {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.networkFailed),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressDialog.hideProgressBar()
                }

            })

        }
    }


    // Job search filter with specific date214e
    fun jobFilterByDate(currentDate: String) {
        val size = filterJobList.size
        val filteredList = mutableListOf<Data>()
        for (i in 0 until size) {
            val jobDate = filterJobList[i].start_date.subSequence(0, 10)
            if (currentDate == jobDate.toString()) {
                filteredList.add(filterJobList[i])
            }
        }
        if (filteredList.isNotEmpty()) {
            jobDetailRecyclerView.visibility = View.VISIBLE
            binding.noJobAssign.visibility = View.GONE
            setUpJobDetailList(filteredList)
        } else {
            jobDetailRecyclerView.visibility = View.GONE
            binding.noJobAssign.visibility = View.VISIBLE
        }
    }

}



