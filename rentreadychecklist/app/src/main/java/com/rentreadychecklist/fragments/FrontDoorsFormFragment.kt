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
import com.rentreadychecklist.adapters.FrontDoorFormAdapter
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.constants.Constants.Companion.frontDoorFixList
import com.rentreadychecklist.databinding.FragmentFrontDoorsFormBinding
import com.rentreadychecklist.formDataSave.FormItemList
import com.rentreadychecklist.formDataSave.FormsDummyData
import com.rentreadychecklist.model.frontdoors.FrontDoors
import com.rentreadychecklist.utils.Helper
import com.rentreadychecklist.viewmodel.AppViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * This class used for show ,edit and save FrontDoor Form Data.
 */
class FrontDoorsFormFragment : Fragment() {

    lateinit var viewBinding: FragmentFrontDoorsFormBinding
    lateinit var adapter: FrontDoorFormAdapter
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentFrontDoorsFormBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        val linearLayout = LinearLayoutManager(context)
        viewBinding.recyclerView.layoutManager = linearLayout
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // update viewModel/Database frontDoors data on click of next button
        viewBinding.next.setOnClickListener {
            Helper.hideKeyBoard(view)
            if (FormsDummyData.frontDoorsData.isNotEmpty()) {
                setDataToFix()
                setOkImages()
                viewModel.updateFrontDoors(
                    FormsDummyData.frontDoorsData, Constants.FORMID
                )
            }
            try {
                view.findNavController()
                    .navigate(R.id.action_frontDoorsFormFragment2_to_garageFormFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewBinding.previous.setOnClickListener {
            Helper.hideKeyBoard(view)
            try {
                view.findNavController()
                    .navigate(R.id.action_frontDoorsFormFragment2_to_outsideFormFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        setDataOnAdapter()
    }

    // set adapter as per the saved data in viewModel/database frontDoors
    private fun setDataOnAdapter() {
        try {
            if (FormsDummyData.frontDoorsData.isNotEmpty()) {
                setFixDataToField(FormsDummyData.frontDoorsData)
                getOkImages(FormsDummyData.frontDoorsData)

                adapter =
                    FrontDoorFormAdapter(
                        requireContext(),
                        FormItemList.frontDoorItems,
                        FormsDummyData.frontDoorsData
                    )
                MainScope().launch {
                    viewBinding.recyclerView
                        .setItemViewCacheSize(FormItemList.frontDoorItems.size)
                    viewBinding.recyclerView.adapter = adapter
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //  save  fix data in frontDoor FixList
    fun setFixDataToField(frontDoorData: List<FrontDoors>) {
        frontDoorFixList.clear()
        for (element in frontDoorData) {
            frontDoorFixList.add(element.fix)
        }
    }

    // set data in fix if already data saved in database fix item in frontDoors fix list
    fun setDataToFix() {
        for (i in 0 until FormsDummyData.frontDoorsData.size) {
            if (FormsDummyData.frontDoorsData[i].fix.fix == getString(R.string.itemFIX)) {
                FormsDummyData.frontDoorsData[i].fix = frontDoorFixList[i]
            }
        }
    }

    // set data in ok  if already data saved in database ok item in frontDoors list
    private fun setOkImages() {
        for (i in 0 until FormsDummyData.frontDoorsData.size) {
            if (FormsDummyData.frontDoorsData[i].ok.ok == getString(R.string.itemOK)) {
                FormsDummyData.frontDoorsData[i].ok = Constants.frontDoorOkImageList[i]
            }
        }
    }

    // save ok item data in frontDoors ok image list

    private fun getOkImages(frontDoorsData: List<FrontDoors>) {
        Constants.frontDoorOkImageList.clear()
        for (element in frontDoorsData) {
            Constants.frontDoorOkImageList.add(element.ok)
        }
    }

}