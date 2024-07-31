package com.example.listerpros.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.listerpros.R
import com.example.listerpros.databinding.FragmentForgetPasswordBinding
import com.example.listerpros.utils.Helper

class ForgetPasswordFragment : Fragment() {

    private lateinit var binding: FragmentForgetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener()
        {
            if (requireActivity().onBackPressedDispatcher.hasEnabledCallbacks()) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        // email validation done
        binding.sendButton.setOnClickListener {
            validationCheck()  //Validation check for email
        }
    }

    private fun validationCheck() {
        val emailAddress = binding.emailAddressForgot.text
        if (emailAddress.toString().isNotEmpty()) {
            if (Helper.isValidEmail(emailAddress.toString()) || Helper.isValidEmailFormat(
                    emailAddress.toString())
            ) {
                Toast.makeText(context,
                    resources.getString(R.string.validEmail),
                    Toast.LENGTH_SHORT).show()
            } else {
                binding.forgotField.error = resources.getString(R.string.invalidEmail)
            }
        } else {
            binding.forgotField.error = resources.getString(R.string.pleaseEnterEmail)
        }

    }

}
