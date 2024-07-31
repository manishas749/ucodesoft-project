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
import com.rentreadychecklist.constants.Constants.Companion.FORMID
import com.rentreadychecklist.databinding.FragmentNumberOfBedroomsBinding
import com.rentreadychecklist.formDataSave.FormsDummyData
import com.rentreadychecklist.utils.Helper
import com.rentreadychecklist.viewmodel.AppViewModel

/**
 * This class used for select number of Bedroom.
 */
class NumberOfBedroomsFragment : Fragment() {

    private lateinit var viewBinding: FragmentNumberOfBedroomsBinding
    private val numberOfBedRooms = arrayOf(1, 2, 3, 4, 5, 6)
    private lateinit var viewModel: AppViewModel
    private var getNumberOfBedroom: Int = 1
    private lateinit var formsDummyData: FormsDummyData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentNumberOfBedroomsBinding
            .inflate(inflater, container, false)
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
            numberOfBedRooms
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
            onNextClick(totalBedSelected)

        }

        viewBinding.previous.setOnClickListener {
            Helper.hideKeyBoard(view)
            try {
                findNavController()
                    .navigate(R.id.action_numberOfBedroomsFragment_to_miscellaneousFromFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    private fun onNextClick(totalBedSelected: Int) {
        if (getNumberOfBedroom < totalBedSelected) {
            val requiredBedroom = totalBedSelected - getNumberOfBedroom
            for (i in 0 until requiredBedroom) {
                val singleBedroom = FormsDummyData()
                singleBedroom.bedroomAdd()
            }
            viewModel.updateBedroom(FormsDummyData.numberOfBedroom, FORMID)
        } else {
            val removeBedroom = getNumberOfBedroom - totalBedSelected
            if (removeBedroom > 0) {
                for (i in 0 until removeBedroom) {
                    FormsDummyData.numberOfBedroom.removeLast()
                }
                viewModel.updateBedroom(FormsDummyData.numberOfBedroom, FORMID)
            }
        }
        try {
            findNavController()
                .navigate(R.id.action_numberOfBedroomsFragment_to_bedroomFormFragment)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun viewModelObserver() {
        try {
            getNumberOfBedroom = FormsDummyData.numberOfBedroom.size
            viewBinding.spinner.setSelection(getNumberOfBedroom - 1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}