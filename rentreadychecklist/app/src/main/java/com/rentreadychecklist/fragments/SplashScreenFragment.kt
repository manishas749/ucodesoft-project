package com.rentreadychecklist.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rentreadychecklist.R
import com.rentreadychecklist.databinding.FragmentSplashScreenBinding

/**
 * Here we used custom SplashScreen
 */
@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {

    lateinit var viewBinding: FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        viewBinding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed(
            {
                findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
            }, 1000
        )
    }
}