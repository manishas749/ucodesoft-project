package com.example.fragmentsproject.fragments

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fragmentsproject.R
import com.example.fragmentsproject.adapter.CustomAdapter
import com.example.fragmentsproject.databinding.FragmentMainBinding
import com.example.fragmentsproject.utils.SharedPreference


class MainFragment : Fragment() {

    lateinit var binding: FragmentMainBinding
    private lateinit var adapter: CustomAdapter
    private lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference = SharedPreference(requireContext())
        binding.logout.setOnClickListener {
            sharedPreference.clearToken()

            findNavController().navigate(R.id.action_mainFragment_to_registerationFragment)


        }
        requiredPermission()
        openCamera()


    }

    private fun openCamera() {
        binding.openCamera.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirmation")
            builder.setMessage("Are you sure you want to continue??")
            builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                findNavController().navigate(R.id.action_mainFragment_to_itemListDialogFragment)
            }
            builder.setNegativeButton(android.R.string.no) { dialog, which ->
               dialog.cancel()
            }
            builder.show()
        }

    }




    private fun requiredPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !=
            PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) !=
            PackageManager.PERMISSION_GRANTED|| ActivityCompat.checkSelfPermission(requireContext(),Manifest.permission.CALL_PHONE)!=
            PackageManager.PERMISSION_GRANTED)
         {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CALL_PHONE),
                101
            )
        }


    }



}