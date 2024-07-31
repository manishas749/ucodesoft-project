package com.ucopdesoft.issuelogger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.databinding.FragmentProfileBinding
import com.ucopdesoft.issuelogger.utils.UserDetailsPreference
import kotlin.math.sign


class ProfileFragment : Fragment() {

private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            com.ucopdesoft.issuelogger.R.layout.fragment_profile,
            container,
            false)
        
       return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logout.setOnClickListener {
            signOut()
            val mainNav =

                requireActivity().findViewById<FragmentContainerView>(R.id.fragmentContainerView)
            Navigation.findNavController(mainNav)
                .navigate(R.id.action_mainFragment_to_mobileVerificationFragment)
        }
    }



    private fun signOut() {
        UserDetailsPreference(requireContext()).clearToken()
        FirebaseAuth.getInstance().signOut()
    }
}