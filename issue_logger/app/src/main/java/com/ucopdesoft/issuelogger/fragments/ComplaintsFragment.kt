package com.ucopdesoft.issuelogger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.adapters.ComplaintAdapter
import com.ucopdesoft.issuelogger.databinding.FragmentComplaintsBinding
import com.ucopdesoft.issuelogger.listeners.OnComplaintClickListener
import com.ucopdesoft.issuelogger.login__authentication.ComplaintListCallBack
import com.ucopdesoft.issuelogger.models.Complaint
import com.ucopdesoft.issuelogger.utils.ComplaintStatus
import com.ucopdesoft.issuelogger.utils.Constants.Companion.COMPLAINT_ID
import com.ucopdesoft.issuelogger.utils.Constants.Companion.DOWNVOTE_COUNT
import com.ucopdesoft.issuelogger.utils.Constants.Companion.UPVOTE_COUNT
import com.ucopdesoft.issuelogger.utils.Constants.Companion.complaintsList
import com.ucopdesoft.issuelogger.utils.Constants.Companion.usersList
import com.ucopdesoft.issuelogger.utils.UserDetailsPreference
import com.ucopdesoft.issuelogger.utils.UsersViewModel
import com.ucopdesoft.issuelogger.viewmodels.ComplaintsViewModel


class ComplaintsFragment : Fragment(), ComplaintListCallBack, OnComplaintClickListener {

    companion object {
        private var selectedPosition = 0
        private val selectedTab = MutableLiveData("")
    }

    private lateinit var binding: FragmentComplaintsBinding
    private lateinit var usersViewModel: UsersViewModel
    private lateinit var complaintsViewModel: ComplaintsViewModel
    private lateinit var adapter: ComplaintAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_complaints, container, false)

        usersViewModel = ViewModelProvider(this)[UsersViewModel::class.java]
        complaintsViewModel = ViewModelProvider(this)[ComplaintsViewModel::class.java]
        adapter =
            ComplaintAdapter(
                requireContext(),
                UserDetailsPreference(requireContext()).getToken()!!,
                this
            )

        usersViewModel.getAllUsers(requireContext())
        complaintsViewModel.getAllComplaints(requireContext(), this)

        observers()
        binding.lifecycleOwner = this

        binding.backPress.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            complaintsRecV.layoutManager = LinearLayoutManager(requireContext())
            complaintsRecV.adapter = adapter

            tabLayout.addTab(tabLayout.newTab().setText(ComplaintStatus.ACTIVE.status))
            tabLayout.addTab(tabLayout.newTab().setText(ComplaintStatus.RESOLVED.status))
            tabLayout.addTab(tabLayout.newTab().setText(ComplaintStatus.REJECTED.status))

            tabLayout.selectTab(tabLayout.getTabAt(selectedPosition))

            tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    selectedPosition = tab.position
                    selectedTab.postValue(tab.text.toString())
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    override fun onResponse(list: MutableList<Complaint>) {
        if (complaintsList.size != list.size) {
            complaintsList = list
        }

        selectedTab.postValue(
            if (selectedTab.value!!.isNotEmpty()) selectedTab.value
            else ComplaintStatus.ACTIVE.status
        )
    }

    override fun onClick(complaint: Complaint, count: String, downVoteCount: String) {
        super.onClick(complaint, count, downVoteCount)

        val bundle = Bundle().apply {
            putString(COMPLAINT_ID, complaint.complaintId)
            putString(UPVOTE_COUNT, count)
            putString(DOWNVOTE_COUNT,downVoteCount)
//            putInt(VIEW_PAGER_VISIBILITY)
        }

        requireActivity().findNavController(R.id.fragmentContainer)
            .navigate(R.id.action_complaintsFragment_to_complaintsDetailFragment, bundle)
    }

    private fun setComplaintsData(list: List<Complaint>, status: String) {
        if (list.isNotEmpty()) {
            binding.noComplaintTv.visibility = View.GONE
            binding.fetchProgressbar.visibility = View.GONE
            binding.complaintsRecV.visibility = View.VISIBLE

            updateRecyclerView(list)
        } else {

            val noComplaint = resources.getString(R.string.no_complaints).split(" ")

            binding.noComplaintTv.text = noComplaint[0].plus(" $status ").plus(noComplaint[1])
            binding.noComplaintTv.visibility = View.VISIBLE
            binding.fetchProgressbar.visibility = View.GONE
            binding.complaintsRecV.visibility = View.GONE
        }
    }

    private fun updateRecyclerView(list: List<Complaint>) {
        binding.complaintsRecV.setItemViewCacheSize(list.size)
        adapter.setData(list, usersList)
        adapter.notifyItemRangeChanged(0, list.size)
    }

    private fun observers() {
        usersViewModel.userList.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                usersList = it
                adapter.setData(complaintsList, it)
            }
        }

        selectedTab.observe(viewLifecycleOwner) { tabStr ->
            when (tabStr) {
                ComplaintStatus.ACTIVE.status -> {
                    setComplaintsData(complaintsList.filter {
                        it.status == ComplaintStatus.ACTIVE.status
                    }, ComplaintStatus.ACTIVE.status)
                }

                ComplaintStatus.RESOLVED.status -> {
                    setComplaintsData(complaintsList.filter {
                        it.status == ComplaintStatus.RESOLVED.status
                    }, ComplaintStatus.RESOLVED.status)
                }

                ComplaintStatus.REJECTED.status -> {
                    setComplaintsData(complaintsList.filter {
                        it.status == ComplaintStatus.REJECTED.status
                    }, ComplaintStatus.REJECTED.status)
                }
            }
        }
    }
}


