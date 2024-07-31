package com.ucopdesoft.issuelogger.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.adapters.ComplaintCommentsAdapter
import com.ucopdesoft.issuelogger.databinding.FragmentCommentsBinding
import com.ucopdesoft.issuelogger.login__authentication.ComplaintListCallBack
import com.ucopdesoft.issuelogger.models.Comment
import com.ucopdesoft.issuelogger.utils.UserDetailsPreference
import com.ucopdesoft.issuelogger.viewmodels.ComplaintsViewModel


class CommentsFragment(private val complaintId: String) : Fragment(), ComplaintListCallBack {

    private lateinit var binding: FragmentCommentsBinding
    private lateinit var complaintsViewModel: ComplaintsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_comments, container, false)
        complaintsViewModel = ViewModelProvider(this)[ComplaintsViewModel::class.java]
        complaintsViewModel.getComplaintsComments(requireContext(), complaintId, this)

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onComplaintCommentsResponse(commentsList: List<Comment>?) {
        super.onComplaintCommentsResponse(commentsList)

        binding.apply {
            if (!commentsList.isNullOrEmpty()) {
                Log.d("COMMENTS_LIST_OF_COMPLAINT", "onComplaintCommentsResponse: $commentsList")
                fetchProgressbar.visibility = View.GONE
                fetchingStatusTv.visibility = View.GONE

                commentsRecV.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
                commentsRecV.adapter = ComplaintCommentsAdapter(
                    commentsList, UserDetailsPreference(requireContext()).getToken()!!
                )

            } else {
                fetchProgressbar.visibility = View.GONE
                fetchingStatusTv.visibility = View.VISIBLE
            //    fetchingStatusTv.text = resources.getString(R.string.no_comments)
            }
        }
    }
}