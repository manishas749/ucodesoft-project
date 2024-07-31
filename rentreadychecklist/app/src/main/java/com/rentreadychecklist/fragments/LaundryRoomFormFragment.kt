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
import com.rentreadychecklist.adapters.LaundryFromAdapter
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.constants.Constants.Companion.FORMID
import com.rentreadychecklist.constants.Constants.Companion.laundryFixList
import com.rentreadychecklist.databinding.FragmentLaundryRoomFormBinding
import com.rentreadychecklist.formDataSave.FormItemList
import com.rentreadychecklist.formDataSave.FormsDummyData
import com.rentreadychecklist.model.laundry.LaundryRoom
import com.rentreadychecklist.utils.Helper
import com.rentreadychecklist.viewmodel.AppViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * This class used for show ,edit and save LaundryRoom Form Data.
 */
class LaundryRoomFormFragment : Fragment() {

    lateinit var viewBinding: FragmentLaundryRoomFormBinding
    lateinit var adapter: LaundryFromAdapter
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentLaundryRoomFormBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        val linearLayout = LinearLayoutManager(context)
        viewBinding.recyclerView.layoutManager = linearLayout
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // update viewModel/Database laundry data on click of next button
        viewBinding.next.setOnClickListener {
            Helper.hideKeyBoard(view)
            if (FormsDummyData.laundryRoomData.isNotEmpty()) {
                setDataToFix()
                setOkImages()
                viewModel.updateLaundryRoom(FormsDummyData.laundryRoomData, FORMID)
            }
            try {
                view.findNavController()
                    .navigate(R.id.action_laundryRoomFormFragment_to_livingRoomFormFragment)
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }
        viewBinding.previous.setOnClickListener {
            Helper.hideKeyBoard(view)

            try {
                view.findNavController()
                    .navigate(R.id.action_laundryRoomFormFragment_to_garageFormFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        setDataOnAdapter()
    }

    // set adapter as per the saved data in viewModel/database laundry
    fun setDataOnAdapter() {
        try {

            if (FormsDummyData.laundryRoomData.isNotEmpty()) {
                setFixDataToField(FormsDummyData.laundryRoomData)
                getOkImages(FormsDummyData.laundryRoomData)

                adapter = LaundryFromAdapter(
                    requireContext(),
                    FormItemList.LaundryRoomItems,
                    FormsDummyData.laundryRoomData
                )
                MainScope().launch {
                    viewBinding.recyclerView.setItemViewCacheSize(FormItemList.LaundryRoomItems.size)
                    viewBinding.recyclerView.adapter = adapter
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    //  save  fix data in laundry FixList
    fun setFixDataToField(laundryRoomData: List<LaundryRoom>) {
        laundryFixList.clear()
        for (element in laundryRoomData) {
            laundryFixList.add(element.fix)
        }
    }

    // set data in fix if already data saved in database fix item in laundry fix list
    fun setDataToFix() {
        for (i in 0 until FormsDummyData.laundryRoomData.size) {
            if (FormsDummyData.laundryRoomData[i].fix.fix == getString(R.string.itemFIX)) {
                FormsDummyData.laundryRoomData[i].fix = laundryFixList[i]
            }
        }
    }

    // set data in ok  if already data saved in database ok item in laundry list
    private fun setOkImages() {
        for (i in 0 until FormsDummyData.laundryRoomData.size) {
            if (FormsDummyData.laundryRoomData[i].ok.ok == getString(R.string.itemOK)) {
                FormsDummyData.laundryRoomData[i].ok = Constants.laundryOkImageList[i]
            }
        }
    }

    // save ok item data in laundry ok image list
    private fun getOkImages(laundryData: List<LaundryRoom>) {
        Constants.laundryOkImageList.clear()
        for (element in laundryData) {
            Constants.laundryOkImageList.add(element.ok)
        }
    }

}