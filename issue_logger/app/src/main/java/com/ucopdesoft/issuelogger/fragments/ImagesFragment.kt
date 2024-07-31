package com.ucopdesoft.issuelogger.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.adapters.ComplaintDetailImageAdapter
import com.ucopdesoft.issuelogger.databinding.FragmentImagesBinding
import com.ucopdesoft.issuelogger.login__authentication.ComplaintListCallBack
import com.ucopdesoft.issuelogger.viewmodels.ComplaintsViewModel


class ImagesFragment(private val complaintId: String) : Fragment(), ComplaintListCallBack {

    private lateinit var binding: FragmentImagesBinding
    private lateinit var complaintsViewModel: ComplaintsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_images, container, false)

        complaintsViewModel = ViewModelProvider(this)[ComplaintsViewModel::class.java]

        complaintsViewModel.getComplaintsImages(requireContext(), complaintId, this)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onComplaintImagesResponse(imagesList: List<String>?) {
        super.onComplaintImagesResponse(imagesList)

        binding.apply {
            if (!imagesList.isNullOrEmpty()) {
                fetchProgressbar.visibility = View.GONE
                fetchingStatusTv.visibility = View.GONE

                imagesRecV.layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                imagesRecV.adapter = ComplaintDetailImageAdapter(imagesList)
            }
        }
    }
}