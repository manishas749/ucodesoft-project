package com.example.fragmentsproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fragmentsproject.R
import com.example.fragmentsproject.data.EmployeeData
import com.example.fragmentsproject.utils.SharedPreference
import com.example.fragmentsproject.data.constants.Companion.ENTERDATALIST
import com.example.fragmentsproject.databinding.FragmentLoginBinding
import com.example.fragmentsproject.viewModel.EmployeeViewModel


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var sharedPreference: SharedPreference
    private lateinit var viewModel: EmployeeViewModel
    private lateinit var employeeList: MutableList<EmployeeData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[EmployeeViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        employeeList = mutableListOf()
        sharedPreference = SharedPreference(requireContext())
        viewModel.readAllData.observe(viewLifecycleOwner)
        { list->

            employeeList = list.toMutableList()



        }

        binding.buttonSignIn.setOnClickListener {
            val userName = binding.userName.text.toString()
            val password = binding.password.text.toString()
            validateData(userName, password)
        }
    }

    private fun validateData(userName: String, password: String) {
        if (userName.isEmpty()) {
            binding.userName.error = "UserName should not be blank"
        } else if (password.isEmpty()) {
            binding.password.error = "Password should not be blank"
        } else {
            var count = 0
            if (employeeList.isNotEmpty()) {
                for (i in 0 until employeeList.size) {
                    if (userName == employeeList[i].userName && (password
                                == employeeList[i].password)
                    ) {
                        var token = "loggedIn"
                        sharedPreference.saveToken(token)
                        Toast.makeText(requireContext(), "Logged in successful", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().navigate(R.id.action_loginFragment2_to_mainFragment)
                    } else {

                        count++

                    }
                }
                if (count == employeeList.size) {
                    Toast.makeText(
                        requireContext(),
                        "UserName & Password is incorrect",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
        }

    }
}


