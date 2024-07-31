package com.example.listerpros.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.listerpros.activities.LoginActivity
import com.example.listerpros.R
import com.example.listerpros.databinding.FragmentMyAccountBinding
import com.example.listerpros.preferences.LoginTokenManager


class MyAccountFragment : Fragment() {

    lateinit var binding: FragmentMyAccountBinding
    private lateinit var loginKey: LoginTokenManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMyAccountBinding.inflate(inflater, container, false)
        loginKey = LoginTokenManager(requireContext())


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // to go on profile fragment
        binding.profileSettings.setOnClickListener{

            findNavController().navigate(R.id.action_myAccountFragment_to_profileSettingsFragment)
        }

        // to go on privacy fragment
        binding.privacy.setOnClickListener{
            findNavController().navigate(R.id.action_myAccountFragment_to_privacyFragment)
        }

        // function for logout
        binding.logout.setOnClickListener{
            userLogout()
        }


    }

    // Logout done to go on login activity
    private fun userLogout(){
        loginKey.clearToken()
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }





}