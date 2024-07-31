package com.ucopdesoft.issuelogger.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.databinding.FragmentNotificationBinding


class NotificationFragment : Fragment() {

    private lateinit var binding:FragmentNotificationBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cancelAction.setOnClickListener {
            if (requireActivity().onBackPressedDispatcher.hasEnabledCallbacks()) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }


        val wordtoSpan: Spannable =
            SpannableString("Your Complaint Pothole has been resolved.")

        wordtoSpan.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.resolved)),
            32,
            41,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        wordtoSpan.setSpan(StyleSpan(Typeface.BOLD_ITALIC), 15, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        wordtoSpan.setSpan(StyleSpan(Typeface.BOLD), 32, 41, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


        binding.todayComplaints.text = wordtoSpan

        val wordToSpanEarlier: Spannable =
            SpannableString("Your Complaint Pothole is currently in-progress.")

        wordToSpanEarlier.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.inProgress)),
            36,
            48,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        wordToSpanEarlier.setSpan(StyleSpan(Typeface.BOLD), 36, 48, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        wordToSpanEarlier.setSpan(StyleSpan(Typeface.BOLD_ITALIC), 15, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.earlierComplaints.text = wordToSpanEarlier

        val wordToSpanEarlier2: Spannable =
            SpannableString("Your Complaint Pothole has been Rejected due to \n invalid content. Please contact us for further \n" +
                    "assistance.")
        wordToSpanEarlier2.setSpan(StyleSpan(Typeface.BOLD_ITALIC), 15, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        wordToSpanEarlier2.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.rejected)),
            32,
            40,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        wordToSpanEarlier2.setSpan(StyleSpan(Typeface.BOLD), 32, 40, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.earlierComplaints2.text = wordToSpanEarlier2

        val wordToSpanEarlier3: Spannable =
            SpannableString("Your Complaint Pothole has been Accepted as per \n your request.")
        wordToSpanEarlier3.setSpan(StyleSpan(Typeface.BOLD_ITALIC), 15, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        wordToSpanEarlier3.setSpan(StyleSpan(Typeface.BOLD_ITALIC), 32, 40, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.earlierComplaints3.text = wordToSpanEarlier3

        val wordTOSpanEarlier4: Spannable =
            SpannableString("Your complaint Pothole has been successfully \n Submitted.")
        wordTOSpanEarlier4.setSpan(StyleSpan(Typeface.BOLD_ITALIC), 15, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.earlierComplaints5.text = wordTOSpanEarlier4











    }

}