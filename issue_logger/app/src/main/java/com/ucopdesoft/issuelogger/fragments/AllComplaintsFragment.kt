package com.ucopdesoft.issuelogger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.adapters.ComplaintAdapter
import com.ucopdesoft.issuelogger.adapters.ItemAddAdapter
import com.ucopdesoft.issuelogger.data.DummyDataAdd
import com.ucopdesoft.issuelogger.databinding.FragmentAllComplaintsBinding
import com.ucopdesoft.issuelogger.listeners.OnComplaintClickListener
import com.ucopdesoft.issuelogger.login__authentication.ComplaintListCallBack
import com.ucopdesoft.issuelogger.models.Complaint
import com.ucopdesoft.issuelogger.models.User
import com.ucopdesoft.issuelogger.utils.Constants.Companion.COMPLAINT_ID
import com.ucopdesoft.issuelogger.utils.UserDetailsPreference
import com.ucopdesoft.issuelogger.utils.UsersViewModel
import com.ucopdesoft.issuelogger.viewmodels.ComplaintsViewModel

class AllComplaintsFragment : Fragment(), ComplaintListCallBack, OnComplaintClickListener {

    private lateinit var binding: FragmentAllComplaintsBinding
    private lateinit var dummyDataAdd: DummyDataAdd
    private lateinit var adapter: ItemAddAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_all_complaints, container, false)
        dummyDataAdd= DummyDataAdd()
       dummyDataAdd.addItem()
        adapter = ItemAddAdapter(requireContext(),dummyDataAdd.ItemData)
        binding.recyler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding.recyler.adapter = adapter



      //  observers()
        binding.lifecycleOwner = this
        binding.viewAllComplaints.setOnClickListener {
            findNavController().navigate(R.id.complaintsFragment)

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewAllCities.setOnClickListener {
            findNavController().navigate(R.id.myCitiesFragment)

        }
    }

  /*  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            /*complaintsRecV.layoutManager = LinearLayoutManager(requireContext())
            complaintsRecV.itemAnimator = null
            complaintsRecV.adapter = adapter

            searchEt.addTextChangedListener {
                filterByLocation(it.toString())
            }*/
        }
    }

    override fun onResponse(list: MutableList<Complaint>) {
        super.onResponse(list)
        if (list.isNotEmpty()) {
            complaintsList = list
            binding.noComplaintTv.visibility = View.GONE
            binding.fetchProgressbar.visibility = View.GONE
            binding.complaintsRecV.visibility = View.VISIBLE
            updateRecyclerView(complaintsList)
        } else {
            binding.noComplaintTv.text = resources.getString(R.string.no_complaints)
            binding.noComplaintTv.visibility = View.VISIBLE
            binding.fetchProgressbar.visibility = View.GONE
            binding.complaintsRecV.visibility = View.GONE
        }
    }

    override fun onClick(complaint: Complaint) {
        super.onClick(complaint)

        val bundle = Bundle().apply {
            putString(COMPLAINT_ID, complaint.complaintId)
        }

        requireActivity().findNavController(R.id.fragmentContainerView)
            .navigate(R.id.action_mainFragment_to_complaintsDetailFragment, bundle)
    }

    private fun filterByLocation(text: String): Boolean {
        updateRecyclerView(complaintsList.filter {
            it.address.lowercase()
                .contains(text.lowercase())
        })
        return true
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
    }*/
}