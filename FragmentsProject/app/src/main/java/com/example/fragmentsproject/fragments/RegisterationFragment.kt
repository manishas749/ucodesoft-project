package com.example.fragmentsproject.fragments

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fragmentsproject.R
import com.example.fragmentsproject.utils.SharedPreference
import com.example.fragmentsproject.data.EmployeeData
import com.example.fragmentsproject.databinding.FragmentRegisterationBinding
import com.example.fragmentsproject.viewModel.EmployeeViewModel


class RegisterationFragment : Fragment() {


    private lateinit var binding:FragmentRegisterationBinding
    private lateinit var sharedPreference: SharedPreference
    private lateinit var viewModel: EmployeeViewModel
    private lateinit var db:SQLiteDatabase


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[EmployeeViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference = SharedPreference(requireContext())



        if (sharedPreference.getToken()!=null)
        {
            findNavController().navigate(R.id.action_registerationFragment_to_mainFragment)
        }


        //enterDataList = arrayListOf()
        binding.buttonSignUp.setOnClickListener {
            var firstName = binding.firstName.text.toString()
            var lastName = binding.lastName.text.toString()
            var userName = binding.userName.text.toString()
            var dateOfBirth = binding.dateOfBirth.text.toString()
            var emailId = binding.emailid.text.toString()
            var password = binding.password.text.toString()
            validateData(firstName,lastName,userName,dateOfBirth,emailId,password)
        }
    }

    private fun validateData(firstName: String, lastName: String, userName: String, dateOfBirth: String, emailId: String, password: String) {

        if (firstName.isEmpty())
        {
            binding.firstName.error = "First Name should not be empty"
        }
        else if (lastName.isEmpty())
        {
            binding.lastName.error = "Last Name should not be empty"
        }
        else if (userName.isEmpty())
        {
            binding.userName.error = "User Name should not be empty"

        }
        else if (dateOfBirth.isEmpty())
        {
            binding.dateOfBirth.error = "Date of birth should not be empty"

        }
        else if (emailId.isEmpty())
        {
            binding.emailid.error = "Email id should not be empty"

        }
        else if (password.isEmpty())
        {
            binding.password.error = "password should not be empty"

        }
        else
        {
            var empData = EmployeeData(0,firstName,lastName,userName,dateOfBirth,emailId,password)
            viewModel.addEmp(empData)
            Toast.makeText(requireContext(),"Data added successfully",Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_registerationFragment_to_loginFragment2)
        }



    }


}