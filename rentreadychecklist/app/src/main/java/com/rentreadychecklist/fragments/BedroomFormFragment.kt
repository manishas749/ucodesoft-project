package com.rentreadychecklist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rentreadychecklist.R
import com.rentreadychecklist.adapters.BedroomChildAdapter
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.constants.Constants.Companion.bedroomFixList
import com.rentreadychecklist.constants.Constants.Companion.bedroomOkImageList
import com.rentreadychecklist.databinding.FragmentBedroomFormBinding
import com.rentreadychecklist.formDataSave.FormItemList
import com.rentreadychecklist.formDataSave.FormsDummyData
import com.rentreadychecklist.model.bedrooms.Bedroom
import com.rentreadychecklist.model.bedrooms.BedroomFix
import com.rentreadychecklist.model.bedrooms.BedroomOk
import com.rentreadychecklist.utils.Helper
import com.rentreadychecklist.viewmodel.AppViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


/**
 * This class used for show ,edit and save Bedroom Form Data.
 */
class BedroomFormFragment : Fragment() {
    lateinit var viewBinding: FragmentBedroomFormBinding
    lateinit var viewModel: AppViewModel
    private lateinit var bedroomChildAdapter: BedroomChildAdapter
    private var bedroomNumber = 1
    private var totalNumberOfBedroom = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentBedroomFormBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        val linearLayout = LinearLayoutManager(context)
        viewBinding.recyclerView.layoutManager = linearLayout
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.next.setOnClickListener {
            Helper.hideKeyBoard(view)
            nextBedRoomPosition()
        }

        viewBinding.previous.setOnClickListener {
            Helper.hideKeyBoard(view)
            previousBedroom()
        }

        viewBinding.editButton.setOnClickListener {
            viewBinding.editButton.visibility = View.GONE
            viewBinding.previous.visibility = View.GONE
            viewBinding.next.visibility = View.GONE
            viewBinding.saveButton.visibility = View.VISIBLE
            viewBinding.bedroomNameEditText.visibility = View.VISIBLE
            viewBinding.formTitle.visibility = View.GONE
            if (FormsDummyData.numberOfBedroom[bedroomNumber - 1].bedroomName != getString(R.string.bedroom)) {
                viewBinding.bedroomNameEditText.setText(FormsDummyData.numberOfBedroom[bedroomNumber - 1].bedroomName)
            } else {
                val bedroomNameNumber = "Bedroom $bedroomNumber"
                viewBinding.bedroomNameEditText.setText(bedroomNameNumber)
            }
            Helper.showKeyBoard(viewBinding.bedroomNameEditText)

        }
        viewBinding.saveButton.setOnClickListener {
            if (viewBinding.bedroomNameEditText.text.isNotEmpty()) {
                viewBinding.saveButton.visibility = View.GONE
                viewBinding.editButton.visibility = View.VISIBLE
                Helper.hideKeyBoard(view)
                saveBedroomName(bedroomNumber)
            } else {
                viewBinding.bedroomNameEditText.error =
                    getString(R.string.please_enter_bedroom_name)
            }


        }





        viewBinding.recyclerView.onFocusChangeListener =
            View.OnFocusChangeListener { v, _ ->
                Helper.hideKeyBoard(v)
            }

        setDataOnAdapter()
    }

    //set Adapter as per the bathroom Position when clicked on the next button
    private fun nextBedRoomPosition() {
        if (bedroomNumber < totalNumberOfBedroom) {
            when (bedroomNumber) {
                1 -> {
                    onNextCLick(1)
                }

                2 -> {
                    onNextCLick(2)
                }

                3 -> {
                    onNextCLick(3)
                }

                4 -> {
                    onNextCLick(4)
                }

                5 -> {
                    onNextCLick(5)
                }

                6 -> {
                    if (FormsDummyData.numberOfBedroom.isNotEmpty()) {
                        setDataToFix()
                        setOkImage()
                        viewModel.updateBedroom(FormsDummyData.numberOfBedroom, Constants.FORMID)
                    }
                    try {
                        findNavController().navigate(R.id.action_bedroomFormFragment_to_numberOfBathroomsFragment)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        } else {
            if (FormsDummyData.numberOfBedroom.isNotEmpty()) {
                setDataToFix()
                setOkImage()
                viewModel.updateBedroom(FormsDummyData.numberOfBedroom, Constants.FORMID)
            }
            try {
                findNavController().navigate(R.id.action_bedroomFormFragment_to_numberOfBathroomsFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun onNextCLick(position: Int) {

        if (FormsDummyData.numberOfBedroom.isNotEmpty()) {
            setDataToFix()
            setOkImage()
            viewModel.updateBedroom(FormsDummyData.numberOfBedroom, Constants.FORMID)
        }


        setDataToAdapterWithPosition(position)
        bedroomNumber = position + 1

        if (FormsDummyData.numberOfBedroom[position].bedroomName != getString(R.string.bedroom)) {
            viewBinding.formTitle.text = FormsDummyData.numberOfBedroom[position].bedroomName
        } else {
            val bedroomNumberName = "Bedroom $bedroomNumber"
            viewBinding.formTitle.text = bedroomNumberName
        }

    }


    private fun saveBedroomName(position: Int) {
        val bedroomName = viewBinding.bedroomNameEditText.text
        FormsDummyData.numberOfBedroom[position - 1].bedroomName = bedroomName.toString()
        viewBinding.formTitle.text = FormsDummyData.numberOfBedroom[position - 1].bedroomName
        viewBinding.bedroomNameEditText.setText("")
        viewBinding.bedroomNameEditText.visibility = View.GONE
        // viewBinding.saveName.visibility = View.GONE
        viewBinding.previous.visibility = View.VISIBLE
        viewBinding.next.visibility = View.VISIBLE
        viewBinding.formTitle.visibility = View.VISIBLE
    }

    //set Adapter as per the bedroom Position when clicked on the previous button

    private fun previousBedroom() {

        when (bedroomNumber) {
            1 -> {
                if (FormsDummyData.numberOfBedroom.isNotEmpty()) {
                    setDataToFix()
                    setOkImage()
                    viewModel.updateBedroom(FormsDummyData.numberOfBedroom, Constants.FORMID)
                }
                try {
                    findNavController().navigate(R.id.action_bedroomFormFragment_to_numberOfBedroomsFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            2 -> {
                onPreviousClick(2)
            }

            3 -> {
                onPreviousClick(3)
            }

            4 -> {
                onPreviousClick(4)
            }

            5 -> {
                onPreviousClick(5)
            }

            6 -> {
                onPreviousClick(6)
            }
        }

    }

    private fun onPreviousClick(position: Int) {
        setDataToAdapterWithPosition(position - 2)
        bedroomNumber = position - 1

        if (FormsDummyData.numberOfBedroom[bedroomNumber - 1].bedroomName != getString(R.string.bedroom)) {
            viewBinding.formTitle.text =
                FormsDummyData.numberOfBedroom[bedroomNumber - 1].bedroomName
        } else {
            val bedroomNumberName = "Bedroom $bedroomNumber"
            viewBinding.formTitle.text = bedroomNumberName
        }

    }

    // set adapter as the saved data in database
    private fun setDataToAdapterWithPosition(position: Int) {
        viewBinding.formTitle.text = FormsDummyData.numberOfBedroom[position].bedroomName
        bedroomChildAdapter = BedroomChildAdapter(
            requireContext(), FormItemList.bed,
            FormsDummyData.numberOfBedroom[position].list, position

        )
        MainScope().launch {
            viewBinding.recyclerView.setItemViewCacheSize(FormsDummyData.numberOfBedroom[position].list.size)
            viewBinding.recyclerView.adapter = bedroomChildAdapter
        }
    }


    private fun setDataOnAdapter() {
        try {
            if (FormsDummyData.numberOfBedroom.isNotEmpty()) {
                setFixDataToField(FormsDummyData.numberOfBedroom)
                getOkImage(FormsDummyData.numberOfBedroom)

                if (FormsDummyData.numberOfBedroom[0].bedroomName != getString(R.string.bedroom)) {
                    viewBinding.formTitle.text = FormsDummyData.numberOfBedroom[0].bedroomName
                } else {
                    val bedroomNumberName = "Bedroom $bedroomNumber"
                    viewBinding.formTitle.text = bedroomNumberName
                }
                totalNumberOfBedroom = FormsDummyData.numberOfBedroom.size

                bedroomChildAdapter = BedroomChildAdapter(
                    requireContext(),
                    FormItemList.bed,
                    FormsDummyData.numberOfBedroom[0].list, 0

                )
                MainScope().launch {
                    viewBinding.recyclerView.setItemViewCacheSize(FormsDummyData.numberOfBedroom[0].list.size)
                    viewBinding.recyclerView.adapter = bedroomChildAdapter
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //save  fix data in bedroomFix list
    fun setFixDataToField(bedroomData: List<Bedroom>) {
        bedroomFixList.clear()
        for (element in bedroomData) {
            val list = mutableListOf<BedroomFix>()
            for (j in 0 until element.list.size) {
                list.add(element.list[j].fix)
            }
            bedroomFixList.add(list)
        }
    }

    // set data in fix if already data saved in database fix item in bedroom list

    fun setDataToFix() {
        for (i in 0 until FormsDummyData.numberOfBedroom.size) {
            for (j in 0 until FormsDummyData.numberOfBedroom[i].list.size) {
                if (FormsDummyData.numberOfBedroom[i].list[j].fix.fix == getString(R.string.itemFIX)) {
                    FormsDummyData.numberOfBedroom[i].list[j].fix = bedroomFixList[i][j]
                }
            }

        }
    }

    // set data in ok  if already data saved in database ok item in bedroom list
    private fun setOkImage() {
        for (i in 0 until FormsDummyData.numberOfBedroom.size) {
            for (j in 0 until FormsDummyData.numberOfBedroom[i].list.size) {

                if (FormsDummyData.numberOfBedroom[i].list[j].ok.ok == getString(R.string.itemOK)) {
                    FormsDummyData.numberOfBedroom[i].list[j].ok = bedroomOkImageList[i][j]
                }
            }
        }
    }


    // save ok item data in bedroomOkImage list
    private fun getOkImage(bedroomData: List<Bedroom>) {
        bedroomOkImageList.clear()
        for (element in bedroomData) {
            val list = mutableListOf<BedroomOk>()
            for (j in 0 until element.list.size) {
                list.add(element.list[j].ok)
            }
            bedroomOkImageList.add(list)
        }
    }

}