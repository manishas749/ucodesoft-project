package com.rentreadychecklist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rentreadychecklist.R
import com.rentreadychecklist.adapters.GarageFormAdapter
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.constants.Constants.Companion.FORMID
import com.rentreadychecklist.constants.Constants.Companion.garageFixList
import com.rentreadychecklist.databinding.FragmentGarageFormBinding
import com.rentreadychecklist.formDataSave.FormItemList
import com.rentreadychecklist.formDataSave.FormsDummyData
import com.rentreadychecklist.model.garage.Garage
import com.rentreadychecklist.utils.Helper
import com.rentreadychecklist.viewmodel.AppViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * This class used for show ,edit and save Garage Form Data.
 */
class GarageFormFragment : Fragment() {

    lateinit var adapter: GarageFormAdapter
    private lateinit var viewBinding: FragmentGarageFormBinding
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentGarageFormBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        val linearLayout = LinearLayoutManager(context)
        viewBinding.recyclerView.layoutManager = linearLayout
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // update viewModel/Database garage list as per data saved on click of next
        viewBinding.next.setOnClickListener {
            Helper.hideKeyBoard(view)
            if (FormsDummyData.garageData.isNotEmpty()) {
                setDataToFix()
                setOkImages()
                viewModel.updateGarage(FormsDummyData.garageData, FORMID)
            }
            try {
                view.findNavController()
                    .navigate(R.id.action_garageFormFragment_to_laundryRoomFormFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewBinding.previous.setOnClickListener {
            Helper.hideKeyBoard(view)
            try {
                view.findNavController()
                    .navigate(R.id.action_garageFormFragment_to_frontDoorsFormFragment2)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        setDataOnAdapter()
    }

    // set adapter as per data already saved in garage list in database
    private fun setDataOnAdapter() {
        try {
            if (FormsDummyData.garageData.isNotEmpty()) {
                setFixDataToField(FormsDummyData.garageData)
                getOkImages(FormsDummyData.garageData)

                adapter =
                    GarageFormAdapter(
                        requireContext(),
                        FormItemList.garageRoomItems,
                        FormsDummyData.garageData
                    )
                MainScope().launch {
                    viewBinding.recyclerView
                        .setItemViewCacheSize(FormItemList.garageRoomItems.size)
                    viewBinding.recyclerView.adapter = adapter
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //  save  fix data in garage  FixList
    fun setFixDataToField(garageData: List<Garage>) {
        garageFixList.clear()
        for (element in garageData) {
            garageFixList.add(element.fix)
        }
    }

    // set data in fix if already data saved in database fix item in garage fix list
    fun setDataToFix() {
        for (i in 0 until FormsDummyData.garageData.size) {
            if (FormsDummyData.garageData[i].fix.fix == getString(R.string.itemFIX)) {
                FormsDummyData.garageData[i].fix = garageFixList[i]
            }
        }
    }

    // set data in ok  if already data saved in database ok item in garage list
    private fun setOkImages() {
        for (i in 0 until FormsDummyData.garageData.size) {
            if (FormsDummyData.garageData[i].ok.ok == getString(R.string.itemOK)) {
                FormsDummyData.garageData[i].ok = Constants.garageOkImageList[i]
            }
        }
    }

    // save ok item data in garage ok image list
    private fun getOkImages(garageData: List<Garage>) {
        Constants.garageOkImageList.clear()
        for (element in garageData) {
            Constants.garageOkImageList.add(element.ok)
        }
    }

}