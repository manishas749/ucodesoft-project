package com.ucopdesoft.issuelogger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.databinding.FragmentHomeBinding
class HomeFragment : Fragment() {

    private lateinit var viewBinding:FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        requireActivity().window.apply {
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.homeScreen)
        }

        viewBinding.buttonSignIn.setOnClickListener {
            findNavController().navigate(R.id.mobileVerificationFragment)
        }

        viewBinding.BtnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_signingUpFragment5)
        }



        return viewBinding.root
    }


}