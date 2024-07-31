package com.rentreadychecklist.fragments


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.rentreadychecklist.R
import com.rentreadychecklist.constants.Constants.Companion.DOCUMENT_LINK
import com.rentreadychecklist.constants.Constants.Companion.INCIDENT_REPORT_LINK
import com.rentreadychecklist.constants.Constants.Companion.VEHICLE_INSURANCE_LINK
import com.rentreadychecklist.constants.Constants.Companion.bathroomFixList
import com.rentreadychecklist.constants.Constants.Companion.bedroomFixList
import com.rentreadychecklist.constants.Constants.Companion.diningRoomFixList
import com.rentreadychecklist.constants.Constants.Companion.fixList
import com.rentreadychecklist.constants.Constants.Companion.frontDoorFixList
import com.rentreadychecklist.constants.Constants.Companion.garageFixList
import com.rentreadychecklist.constants.Constants.Companion.greatRoomFixList
import com.rentreadychecklist.constants.Constants.Companion.kitchenFixList
import com.rentreadychecklist.constants.Constants.Companion.laundryFixList
import com.rentreadychecklist.constants.Constants.Companion.livingRoomFixList
import com.rentreadychecklist.constants.Constants.Companion.miscellaneousFixList
import com.rentreadychecklist.constants.Constants.Companion.newAndSavedChecklist
import com.rentreadychecklist.databinding.FragmentHomeBinding
import com.rentreadychecklist.db.AppData
import com.rentreadychecklist.formDataSave.FormsDummyData
import com.rentreadychecklist.model.outside.Outside
import com.rentreadychecklist.viewmodel.AppViewModel
import java.io.File

/**
 * This class used for show Home Screen to user.
 */
class HomeFragment : Fragment() {


    private lateinit var formsDummyData: FormsDummyData
    lateinit var viewBinding: FragmentHomeBinding
    private lateinit var viewModel: AppViewModel
    private lateinit var outsideItemList: MutableList<Outside>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        formsDummyData = FormsDummyData()
        outsideItemList = mutableListOf()
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermission()    //permission for read and write
        makeFolder()

        viewBinding.newChecklist.setOnClickListener {
            createNewChecklist(view)
        }

        viewBinding.savedChecklist.setOnClickListener {
            try {
                view.findNavController()
                    .navigate(R.id.action_homeFragment_to_savedChecklistFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewBinding.insurance.setOnClickListener {
            openLink(VEHICLE_INSURANCE_LINK)

        }

        viewBinding.other.setOnClickListener {
            openLink(INCIDENT_REPORT_LINK)

        }

        viewBinding.doc.setOnClickListener {
            openLink(DOCUMENT_LINK)
        }
    }

// To open external links given by the client

    private fun openLink(url: String) {
        val openURL = Intent(Intent.ACTION_VIEW)
        openURL.data = Uri.parse(url)
        startActivity(openURL)
    }


    //while clicking new checklist empty data saved in viewModel/Database in all the lists
    private fun createNewChecklist(view: View) {
        formsDummyData.clearConstantData()
        newAndSavedChecklist = context?.resources?.getString(R.string.newList).toString()
        formsDummyData.homeScreenDataAdd()
        formsDummyData.outsideDataAdd()
        formsDummyData.frontDoorsDataAdd()
        formsDummyData.garageDataAdd()
        formsDummyData.laundryRoomAdd()
        formsDummyData.livingRoomAdd()
        formsDummyData.greatRoomAdd()
        formsDummyData.diningRoomAdd()
        formsDummyData.kitchenAdd()
        formsDummyData.miscellaneousAdd()
        formsDummyData.bedroomAdd()
        formsDummyData.bathroomAdd()
        clearConstantData()
        val addForm = AppData(
            0,
            FormsDummyData.homeScreenData,
            FormsDummyData.outsideData,
            FormsDummyData.frontDoorsData,
            FormsDummyData.garageData,
            FormsDummyData.laundryRoomData,
            FormsDummyData.livingRoomData,
            FormsDummyData.greatRoomData,
            FormsDummyData.diningRoomData,
            FormsDummyData.kitchenData,
            FormsDummyData.miscellaneousData,
            FormsDummyData.numberOfBedroom,
            FormsDummyData.numberOfBathroom
        )
        viewModel.insertFormInfo(addForm)
        view.findNavController().navigate(R.id.action_homeFragment_to_navigationDrawerFragment)
    }

    // check permissions for camera storage
    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireContext() as Activity,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                101
            )
        }
    }

    private fun makeFolder() {

        val path = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            context?.resources?.getString(R.string.rent_ready).toString()
        )
        if (!path.exists()) {
            path.mkdir()
        }
    }

    // To clear constant data

    private fun clearConstantData() {
        fixList.clear()
        garageFixList.clear()
        bedroomFixList.clear()
        bathroomFixList.clear()
        frontDoorFixList.clear()
        laundryFixList.clear()
        livingRoomFixList.clear()
        greatRoomFixList.clear()
        diningRoomFixList.clear()
        kitchenFixList.clear()
        miscellaneousFixList.clear()
    }

}