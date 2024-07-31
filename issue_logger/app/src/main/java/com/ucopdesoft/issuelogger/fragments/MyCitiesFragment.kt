package com.ucopdesoft.issuelogger.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.adapters.ItemAddAdapter
import com.ucopdesoft.issuelogger.adapters.ItemAddAdapterCities
import com.ucopdesoft.issuelogger.data.DummyDataAdd
import com.ucopdesoft.issuelogger.databinding.FragmentComplaintsBinding
import com.ucopdesoft.issuelogger.databinding.FragmentMyCitiesBinding


class MyCitiesFragment : Fragment() {

    private lateinit var binding: FragmentMyCitiesBinding
    private lateinit var adapter: ItemAddAdapterCities
    private lateinit var dummyDataAdd: DummyDataAdd





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_my_cities, container, false)
        dummyDataAdd= DummyDataAdd()
        dummyDataAdd.addItemCities()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backPress.setOnClickListener {
            findNavController().popBackStack()
        }


        adapter = ItemAddAdapterCities(requireContext(),dummyDataAdd.ItemData)
        binding.citiesRecycler.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        binding.citiesRecycler.adapter = adapter

    }


}