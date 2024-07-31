package com.example.dmcremalert.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dmcremalert.R
import com.example.dmcremalert.preference.SharedPreference


class HomeFragment : Fragment() {

    private lateinit var sharedPreference: SharedPreference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedPreference = SharedPreference(requireContext())
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    fun reset() {
        if (activity == null) {
            return
        }
        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }


}