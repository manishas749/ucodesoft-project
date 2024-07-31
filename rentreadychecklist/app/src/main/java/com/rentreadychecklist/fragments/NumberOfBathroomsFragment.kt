package com.rentreadychecklist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rentreadychecklist.R
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.databinding.FragmentNumberOfBathroomsBinding
import com.rentreadychecklist.formDataSave.FormsDummyData
import com.rentreadychecklist.utils.Helper
import com.rentreadychecklist.viewmodel.AppViewModel

/**
 * This class used for select number of Bathroom.
 */
class NumberOfBathroomsFragment : Fragment() {

    private lateinit var viewBinding: FragmentNumberOfBathroomsBinding
    private val numberOfBathRooms = arrayOf(1, 2, 3, 4, 5, 6)
    private lateinit var viewModel: AppViewModel
    private var getNumberOfBathroom: Int = 1
    private lateinit var formsDummyData: FormsDummyData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentNumberOfBathroomsBinding.inflate(
            inflater, container,
            false
        )
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        formsDummyData = FormsDummyData()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var totalBedSelected = 0


        val arrayAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            numberOfBathRooms
        )
        viewBinding.spinner.adapter = arrayAdapter

        viewModelObserver()

        viewBinding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                totalBedSelected = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        viewBinding.next.setOnClickListener {
            Helper.hideKeyBoard(view)
            onNextCLick(totalBedSelected)
        }

        viewBinding.previous.setOnClickListener {

            Helper.hideKeyBoard(view)
            try {
                findNavController()
                    .navigate(R.id.action_numberOfBathroomsFragment_to_bedroomFormFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun onNextCLick(totalBedSelected: Int) {
        if (getNumberOfBathroom < totalBedSelected) {
            val requiredBedroom = totalBedSelected - getNumberOfBathroom
            // formsDummyData.bathroomAdd()
            for (i in 0 until requiredBedroom) {
                val singleBedroom = FormsDummyData()
                singleBedroom.bathroomAdd()
            }
            viewModel.updateBathroom(FormsDummyData.numberOfBathroom, Constants.FORMID)
        } else {
            val removeBedroom = getNumberOfBathroom - totalBedSelected
            if (removeBedroom > 0) {
                for (i in 0 until removeBedroom) {
                    FormsDummyData.numberOfBathroom.removeLast()
                }
                FormsDummyData.numberOfBathroom
            }
        }

        try {
            findNavController()
                .navigate(R.id.action_numberOfBathroomsFragment_to_bathroomFormFragment)
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    private fun viewModelObserver() {
        try {
            getNumberOfBathroom = FormsDummyData.numberOfBathroom.size
            viewBinding.spinner.setSelection(getNumberOfBathroom - 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}