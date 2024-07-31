package com.example.listerpros.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.listerpros.R
import com.example.listerpros.`interface`.MyJobTaskListClickListener
import com.example.listerpros.adapter.AddNotesAdapter
import com.example.listerpros.adapter.MyJobTaskListAdapter
import com.example.listerpros.constants.Constants.Companion.JOB_TASK_STATUS_COUNT
import com.example.listerpros.databinding.FragmentJobDetailsBinding
import com.example.listerpros.model.MyJobTask
import com.example.listerpros.model.getjobdetails.*
import com.example.listerpros.model.locationstatus.ResponseLocationStatus
import com.example.listerpros.model.updatejobstatus.UpdateJobStatus
import com.example.listerpros.preferences.JobDetailTokenManager
import com.example.listerpros.utils.Helper
import com.example.listerpros.utils.ProgressBarDialog
import com.example.listerpros.viewmodel.JobDetailsViewModel
import com.example.listerpros.viewmodel.UpdateJobStatusViewModel
import com.example.listerpros.viewmodel.UpdateLocationStatusViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class JobDetailsFragment : Fragment(), MyJobTaskListClickListener {

    private lateinit var binding: FragmentJobDetailsBinding
    private lateinit var tokenManager: JobDetailTokenManager
    private lateinit var addNoteAdapter: AddNotesAdapter
    private lateinit var myJobTaskRecyclerView: RecyclerView
    private lateinit var myJobTaskList: ArrayList<MyJobTask>
    private lateinit var viewModel: JobDetailsViewModel
    private lateinit var progressDialog: ProgressBarDialog
    private lateinit var updateLocationStatusViewModel: UpdateLocationStatusViewModel
    private lateinit var updateJobStatusViewModel: UpdateJobStatusViewModel

    private var location: String? = null
    private var phoneNumber: String? = null
    private var messageNumber: String? = null
    private var jobStatus: String? = null
    private var jobId: String = ""
    private var title: String? = null
    private var jobTaskListSize: Int? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentJobDetailsBinding.inflate(inflater, container, false)

        tokenManager = JobDetailTokenManager(requireContext())
        viewModel = ViewModelProvider(this)[JobDetailsViewModel::class.java]
        updateLocationStatusViewModel =
            ViewModelProvider(this)[UpdateLocationStatusViewModel::class.java]
        updateJobStatusViewModel = ViewModelProvider(this)[UpdateJobStatusViewModel::class.java]

        progressDialog = ProgressBarDialog(requireContext())
        // progressDialog.showProgressBar()

        myJobTaskList = ArrayList()
        JOB_TASK_STATUS_COUNT = 0
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jobId = requireArguments().getInt("jobId").toString()
        viewModel.jobDetails(jobId)
        jobDetailsObserver()

        Glide.with(requireContext()).load(R.drawable.animate_job).into(binding.noJobFound)

        if (!updateLocationStatusViewModel.responseUpdateLocationStatus.hasObservers()) {
            updateLocationStatusObserver()
        }

        //fetchDetails()          //fetch details from recycleView get Argument and display
        completeTaskButtonEnable()

        fetchCurrentDate()        // current date fetch and display in textBox


        binding.selectStatus.setOnClickListener()
        {
            overlayStatus()          //select status on the way or reached
        }

        binding.call.setOnClickListener()
        {
            fetchPhoneDetail()                //will open phone on click of phone icon and number displayed
        }
        binding.message.setOnClickListener()
        {
            fetchMessageDetail()              // will open message for same number
        }
        binding.location.setOnClickListener()
        {
            fetchLocation()                  //will open google map for given location
        }

        binding.backPress.setOnClickListener()
        {
            onBackPressed()
        }

        // to navigate on Add note page
        binding.addNote.setOnClickListener()
        {
            val bundle = Bundle()
            bundle.putString("jobId", jobId)
            bundle.putString("title", title)
            val addNotesFragment = AddNotesFragment()
            addNotesFragment.arguments = bundle
            Navigation.findNavController(view).navigate(R.id.addNotesFragment, args = bundle)
        }

        // will complete button if all the tasks has been done
        binding.completeTaskButton.setOnClickListener {
            progressDialog.showProgressBar()
            updateJobStatusViewModel.updateJobStatus(
                jobId,
                resources.getString(R.string.job_status_completed)
            )
            updateJobStatusObserver()
        }


    }

    private fun updateLocationStatusObserver() {
        updateLocationStatusViewModel.responseUpdateLocationStatus.observe(
            viewLifecycleOwner,
            Observer { call ->
                call?.clone()?.enqueue(object : Callback<ResponseLocationStatus> {
                    override fun onResponse(
                        call: Call<ResponseLocationStatus>,
                        response: Response<ResponseLocationStatus>,
                    ) {
                        if (response.isSuccessful) {
                            val responseStatus =
                                response.body()?.data?.location_status_detail?.location_status.toString()
                            if (responseStatus == "R") {
                                binding.selectStatus.setText(R.string.reached)
                            } else {
                                binding.selectStatus.setText(R.string.on_the_way_status)
                            }
                            progressDialog.hideProgressBar()

                        } else {
                            Toast.makeText(
                                context,
                                resources.getString(R.string.tryAgain),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseLocationStatus>, t: Throwable) {
                        Toast.makeText(
                            context,
                            resources.getString(R.string.noInternet),
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                })
            })
    }

    private fun updateJobStatusObserver() {
        updateJobStatusViewModel.responseUpdateJobStatus.observe(
            viewLifecycleOwner,
            Observer { call ->
                call?.enqueue(object : Callback<UpdateJobStatus> {
                    override fun onResponse(
                        call: Call<UpdateJobStatus>,
                        response: Response<UpdateJobStatus>
                    ) {
                        if (response.isSuccessful) {
                            val status = response.body()?.data?.status
                            progressDialog.hideProgressBar()
                            Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                            binding.completeTaskButton.isClickable = false
                            binding.completeTaskButton.text =
                                resources.getString(R.string.job_status_completed)
                        } else {
                            Toast.makeText(
                                context,
                                resources.getString(R.string.tryAgain),
                                Toast.LENGTH_SHORT
                            ).show()
                            progressDialog.hideProgressBar()
                        }
                    }

                    override fun onFailure(call: Call<UpdateJobStatus>, t: Throwable) {
                        Toast.makeText(
                            context,
                            resources.getString(R.string.networkFailed),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialog.hideProgressBar()
                    }

                })
            })

    }

    private fun jobDetailsObserver() {
        viewModel.responseJobDetails.observe(viewLifecycleOwner, Observer { call ->
            call?.enqueue(object : Callback<ResponseJobDetails> {
                override fun onResponse(
                    call: Call<ResponseJobDetails>,
                    response: Response<ResponseJobDetails>
                ) {
                    if (response.isSuccessful) {
                        val jobDetails = response.body()?.data
                        setUpJobDetails(jobDetails)

                        // binding.noJobFound.visibility = View.GONE
                        val transition: Transition = Fade()
                        transition.duration = 2000
                        transition.addTarget(binding.jobDetailsLayout)
                        TransitionManager.beginDelayedTransition(binding.root, transition)
                        binding.jobDetailsLayout.visibility = View.VISIBLE
                        // binding.noJobFound.visibility = View.GONE

                        progressDialog.hideProgressBar()
                        hideJobDetailIcon()
                    } else {
                        Log.d("jobDetails", response.code().toString())
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.noteFound),
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()
                    }
                }

                override fun onFailure(call: Call<ResponseJobDetails>, t: Throwable) {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.noInternet),
                        Toast.LENGTH_SHORT
                    ).show()
                    onBackPressed()
                }

            })
        })
    }

    private fun setUpJobDetails(jobDetailsData: Data?) {

        title = jobDetailsData?.title
        binding.jobDetailDes.text = jobDetailsData?.title
        jobTaskListSize = jobDetailsData?.tasks?.size
        jobStatus = jobDetailsData?.status
        jobStatusCheck(jobStatus.toString())

        val preferredShootDate = jobDetailsData?.preferred_shoot_date?.subSequence(0, 10)
        preferredShootDateFormat(preferredShootDate.toString())

        binding.customerName.text = jobDetailsData?.occupant_name
        binding.scheduleTime.text = jobDetailsData?.schedule_time
        binding.accessMethod.text = jobDetailsData?.let { capitalizeString(it.access_method) }
        binding.occupancy.text = jobDetailsData?.let { capitalizeString(it.occupancy) }
        binding.address.text = jobDetailsData?.location
        binding.gateCodeInt.text = jobDetailsData?.gate_code

        val locationStatus: String? = jobDetailsData?.location_status?.location_status
        locationStatusChecked(locationStatus)

        phoneNumber = jobDetailsData?.occupant_contact
        messageNumber = jobDetailsData?.occupant_contact
        location = jobDetailsData?.location

        val jobTask = jobDetailsData?.tasks
        setUpJobTask(jobTask)

        val jobNotes = jobDetailsData?.notes
        setUpNotesAdapter(jobNotes)


    }

    private fun capitalizeString(string: String): String {
        val words = string.split("_").toMutableList()
        var output = ""
        for (word in words) {
            output += word.replaceFirstChar(Char::uppercase) + " "
        }
        output = output.trim()
        return output
    }

    private fun locationStatusChecked(string: String?) {
        when (string) {
            "R" -> binding.selectStatus.text = resources.getString(R.string.location_update_reached)
            "O" -> binding.selectStatus.text =
                resources.getString(R.string.location_update_on_the_way)
            else -> binding.selectStatus.text = resources.getString(R.string.on_the_way)
        }
    }

    private fun preferredShootDateFormat(string: String) {
        val sfd = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(string)
        val preferredShootDateFormat =
            SimpleDateFormat("LLLL dd, yyyy", Locale.getDefault()).format(sfd!!)
        binding.preferredShootDate.text = preferredShootDateFormat.toString()
    }

    private fun setUpJobTask(jobTask: List<Task>?) {
        myJobTaskRecyclerView = binding.myJobTaskRecycleView
        val linearLayout = LinearLayoutManager(context)
        myJobTaskRecyclerView.layoutManager = linearLayout
        val myJobTaskMyJobTaskListAdapter =
            jobTask?.let { MyJobTaskListAdapter(requireContext(), it, this, this, this) }
        myJobTaskRecyclerView.adapter = myJobTaskMyJobTaskListAdapter

    }

    private fun setUpNotesAdapter(jobNotes: List<Note>?) {
        addNoteAdapter = jobNotes?.let { AddNotesAdapter(jobNotes, title!!, jobId) }!!
        val linearLayout = LinearLayoutManager(context)
        binding.addNoteRecycleView.layoutManager = linearLayout
        binding.addNoteRecycleView.adapter = addNoteAdapter
    }

    private fun fetchCurrentDate() {
        binding.currentDate.text = Helper.getCurrentDate()[3]
    }

    private fun overlayStatus() {
        val builder = AlertDialog.Builder(context)
        val view: View = layoutInflater.inflate(R.layout.dialog_job_detail_select_status, null)
        val onTheWay = view.findViewById<TextView>(R.id.onTheWay)
        val reached = view.findViewById<TextView>(R.id.reached)
        builder.setView(view)
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
        onTheWay.setOnClickListener()
        {

            progressDialog.showProgressBar()
            updateLocationStatusViewModel.updateLocationStatusResponse(jobId, "O")
            alertDialog.dismiss()
        }
        reached.setOnClickListener()
        {
            progressDialog.showProgressBar()
            updateLocationStatusViewModel.updateLocationStatusResponse(jobId, "R")
            alertDialog.dismiss()
        }

    }

    private fun fetchMessageDetail() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("sms:$messageNumber")
        intent.putExtra("sms_body", "Type Here")
        startActivity(intent)
    }

    private fun fetchPhoneDetail() {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(phoneNumber)))
        startActivity(intent)
    }

    private fun fetchLocation() {
        val url = "https://www.google.com/maps/search/?api=1&query=$location"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }


    // Interface implement for complete button
    override fun onCellClickListener() {
        completeTaskButtonEnable()
    }

    //if all the task is done then we enable the button
    private fun completeTaskButtonEnable() {
        Log.d("donecom", JOB_TASK_STATUS_COUNT.toString())
        Log.d("donecom", jobTaskListSize.toString())
        if (JOB_TASK_STATUS_COUNT == jobTaskListSize) {
            Log.d("donecom", "completed")
            JOB_TASK_STATUS_COUNT = 0
            binding.completeTaskButton.isEnabled = true
            binding.completeTaskButton.setTextColor(Color.WHITE)
        }
    }

    //when user details not given we will hide location, phone number  and message icon
    private fun hideJobDetailIcon() {
        if (location == null || location.toString().isEmpty()) {
            binding.location.visibility = View.GONE
        }
        if (messageNumber == null || messageNumber.toString().isEmpty()) {
            binding.message.visibility = View.GONE
        }
        if (phoneNumber == null || phoneNumber.toString().isEmpty()) {
            binding.call.visibility = View.GONE
        }
    }

    private fun jobStatusCheck(string: String?) {

        if (string.toString() == resources.getString(R.string.job_status_completed)) {
            binding.completeTaskButton.isClickable = false
            binding.completeTaskButton.text = resources.getString(R.string.job_status_completed)

        }
    }

    fun onBackPressed() {
        progressDialog.hideProgressBar()
        if (requireActivity().onBackPressedDispatcher.hasEnabledCallbacks()) {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}
