package com.ucopdesoft.issuelogger.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.utils.UserDetailsPreference

class LogoFragment : Fragment() {

    val getActionId: (Boolean) -> Int = {
        if (it) R.id.action_logoFragment_to_mainFragment
        else R.id.action_logoFragment_to_homeFragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(
                getActionId(
                    !UserDetailsPreference(requireContext()).getToken().isNullOrEmpty()
                )
            )
        }, 1000)
    }
}