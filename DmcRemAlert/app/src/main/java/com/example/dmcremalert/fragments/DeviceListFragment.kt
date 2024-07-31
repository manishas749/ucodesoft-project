package com.example.dmcremalert.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dmcremalert.Permission
import com.example.dmcremalert.adapter.DeviceListAdapter
import com.example.dmcremalert.databinding.FragmentDeviceListBinding
import com.example.dmcremalert.viewmodel.DeviceListViewModel


class DeviceListFragment(
) : Fragment() {
    private lateinit var binding: FragmentDeviceListBinding
    private lateinit var permission: Permission
    private lateinit var viewModel: DeviceListViewModel
    private lateinit var adapter: DeviceListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDeviceListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[DeviceListViewModel::class.java]
        permission = Permission(requireActivity(), requireContext(), viewModel)
        permission.requestBlePermissions(requireActivity(), 1)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.startScanning()
        permission.permission()
        binding.recycle.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        viewModel.bluetoothDeviceLiveData.observe(viewLifecycleOwner) {
            adapter = DeviceListAdapter(requireContext(), it,viewModel)
            binding.recycle.adapter = adapter

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unregisterReceiver()
    }

    override fun onStop() {
        super.onStop()
        viewModel.unregisterReceiver()
    }


}