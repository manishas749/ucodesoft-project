package com.ucopdesoft.issuelogger.fragments

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.adapters.ViewPagerAdapter
import com.ucopdesoft.issuelogger.databinding.FragmentComplaintsDetailBinding
import com.ucopdesoft.issuelogger.listeners.ComplaintsListener
import com.ucopdesoft.issuelogger.login__authentication.ComplaintListCallBack
import com.ucopdesoft.issuelogger.models.Complaint
import com.ucopdesoft.issuelogger.utils.ComplaintStatus
import com.ucopdesoft.issuelogger.utils.Constants.Companion.COMPLAINT_ID
import com.ucopdesoft.issuelogger.utils.Constants.Companion.DOWNVOTE_COUNT
import com.ucopdesoft.issuelogger.utils.Constants.Companion.UPVOTE_COUNT
import com.ucopdesoft.issuelogger.utils.Constants.Companion.USERNAME
import com.ucopdesoft.issuelogger.utils.Constants.Companion.complaintsList
import com.ucopdesoft.issuelogger.viewmodels.ComplaintsViewModel
import java.text.SimpleDateFormat
import java.util.Locale


class ComplaintsDetailFragment : Fragment(), ComplaintListCallBack, ComplaintsListener {

    private lateinit var binding: FragmentComplaintsDetailBinding
    private lateinit var complaintsViewModel: ComplaintsViewModel
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    var dotCount = 0
    private lateinit var adapter: ViewPagerAdapter
    private var dots: Array<ImageView?> = arrayOf()


    private lateinit var complaint: Complaint

    private var complaintId: String? = null
    private var upvote_Count: String? = null
    private var downvote_Count: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_complaints_detail, container, false)
        complaintsViewModel = ViewModelProvider(this)[ComplaintsViewModel::class.java]

        complaintId = arguments?.getString(COMPLAINT_ID)
        upvote_Count = arguments?.getString(UPVOTE_COUNT)
        downvote_Count = arguments?.getString(DOWNVOTE_COUNT)


        complaint = complaintsList.find { it.complaintId == complaintId!! }!!
        // fetchLikeDisLikeValueFromFirebaseDb(complaint)

        complaintsViewModel.getComplaintsImages(requireContext(), complaintId!!, this)



        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            backButton.setOnClickListener {
                findNavController().popBackStack()
            }

            ComplaintViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    Log.d("implemented", "implemented")


                }


                override fun onPageSelected(position: Int) {
                    for (i in 0 until dotCount) {
                        dots[i]?.setImageResource(R.drawable.non_active_dots)
                        dots[position]?.setImageResource(R.drawable.active_dots)

                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    Log.d("implemented", "implemented")
                }

            })




            complaintUserName.text = USERNAME

            ComplaintDate.text = if (complaint.date != 0L) {
                ComplaintDate.visibility = View.VISIBLE
                SimpleDateFormat(
                    "MMMM d, yyyy K:mm aa",
                    Locale.getDefault()
                ).format(complaint.date)
            } else {
                ComplaintDate.visibility = View.GONE
                ""
            }

            val color = when (complaint.status) {
                ComplaintStatus.ACTIVE.status -> {
                    Color.parseColor("#6750A4")
                }

                ComplaintStatus.REJECTED.status -> {
                    Color.parseColor("#FF0000")
                }

                ComplaintStatus.RESOLVED.status -> {
                    Color.parseColor("#0B9C1A")
                }

                else -> {
                    Color.BLACK
                }
            }
            progressIndicator.setColorFilter(color)
            complaintStatus.setTextColor(color)
            complaintStatus.text = complaint.status

            if (complaint.address.isNotEmpty()) {
                fetchLocation.text = complaint.address
                fetchLocation.visibility = View.VISIBLE
            } else {
                fetchLocation.visibility = View.GONE
            }

            if (complaint.complaintDescription.isNotEmpty()) {
                addDescription.text = complaint.complaintDescription
                addDescription.visibility = View.VISIBLE
                Description.visibility = View.VISIBLE
            } else {
                addDescription.visibility = View.GONE
                Description.visibility = View.GONE

            }
            if (upvote_Count != "0") {
                binding.upvoteCount.text = upvote_Count
                binding.upvoteCount.setTextColor(requireContext().resources.getColor(R.color.upvote))
                binding.upvote.setTextColor(requireContext().resources.getColor(R.color.upvote))

            } else {
                binding.upvoteCount.setTextColor(requireContext().resources.getColor(R.color.downvote))
                binding.upvote.setTextColor(requireContext().resources.getColor(R.color.downvote))

            }

            if (downvote_Count != "0") {
                binding.downVoteCount.text = downvote_Count
                binding.downVoteCount.setTextColor(requireContext().resources.getColor(R.color.downvote_red))
                binding.downvote.setTextColor(requireContext().resources.getColor(R.color.downvote_red))

            } else {
                binding.downVoteCount.setTextColor(requireContext().resources.getColor(R.color.downvote))
                binding.downvote.setTextColor(requireContext().resources.getColor(R.color.downvote))

            }


        }
    }


    override fun onComplaintImagesResponse(imagesList: List<String>?) {
        super.onComplaintImagesResponse(imagesList)

        if (imagesList != null) {
            binding.ComplaintViewPager.visibility = View.VISIBLE
            adapter = ViewPagerAdapter(imagesList)
            dotCount = adapter.itemCount
            dots = arrayOfNulls(dotCount)
            for (i in 0 until dotCount) {
                dots[i] = ImageView(requireContext())
                dots[i]?.setImageResource(R.drawable.non_active_dots)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(8, 0, 8, 0)
                binding.SliderDots.addView(dots[i], params)
            }

            binding.ComplaintViewPager.adapter = adapter
        } else {
            binding.ComplaintViewPager.visibility = View.GONE
        }
    }


}