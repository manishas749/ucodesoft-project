package com.ucopdesoft.issuelogger.fragments

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.FirebaseDatabase
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.databinding.FragmentSigningUpBinding
import com.ucopdesoft.issuelogger.utils.Helper
import com.ucopdesoft.issuelogger.utils.Tables


class SigningUpFragment : Fragment() {
    private lateinit var viewBinding: FragmentSigningUpBinding
    private var dataBase: FirebaseDatabase = FirebaseDatabase.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = DataBindingUtil.inflate(
            inflater,
            com.ucopdesoft.issuelogger.R.layout.fragment_signing_up,
            container,
            false
        )

        val wordtoSpan: Spannable =
            SpannableString("I agree with all the Term & Conditions of Ayodhya311 app.")

        wordtoSpan.setSpan(
            ForegroundColorSpan(resources.getColor(com.ucopdesoft.issuelogger.R.color.terms)),
            21,
            38,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )


        requireActivity().window.apply {
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
            val wic = WindowInsetsControllerCompat(this, decorView)
            wic.isAppearanceLightStatusBars = true
            navigationBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        }

        viewBinding.terms.text = wordtoSpan

        viewBinding.login.setOnClickListener {
            findNavController().navigate(R.id.action_signingUpFragment_to_mobileVerificationFragment)
        }

        viewBinding.signUp.setOnClickListener {
            val emailId = viewBinding.emailid.text.toString()
            val phoneNumber = viewBinding.phoneNumber.text.toString()
            val aadhar = viewBinding.aadhar.text.toString()
            validate(emailId, phoneNumber, aadhar)
        }









        return viewBinding.root
    }

    fun validate(emailId: String, phoneNumber: String, aadharCard: String) {
        viewBinding.emailid.error = null
        viewBinding.aadhar.error = null
        viewBinding.phoneNumber.error = null
        if (emailId.isEmpty()) {
            viewBinding.emailid.error = resources.getString(R.string.please_enter_email)
        } else if (!Helper.isValidEmail(emailId) || !Helper.isValidEmailFormat(emailId)) {
            viewBinding.emailid.error = resources.getString(R.string.invalidEmail)
        } else if (phoneNumber.isEmpty()) {
            viewBinding.phoneNumber.error = resources.getString(R.string.blank_phoneNumber)

        } else if (aadharCard.isEmpty()) {
            viewBinding.aadhar.error = resources.getString(R.string.blank_aadhar)
        } else if (!viewBinding.checkbox.isChecked) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.agree_terms_conditions),
                Toast.LENGTH_SHORT
            ).show()

        } else {
            viewBinding.emailid.error = null
            viewBinding.phoneNumber.error = null
            viewBinding.aadhar.error = null
            viewBinding.phoneNumber.error = null

            Toast.makeText(
                requireContext(),
                resources.getString(R.string.user_created),
                Toast.LENGTH_SHORT
            ).show()

           // createUser(emailId, phoneNumber, aadharCard,userName)
        }

    }




    }
