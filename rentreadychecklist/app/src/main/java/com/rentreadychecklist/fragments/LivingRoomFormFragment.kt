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
import com.rentreadychecklist.adapters.LivingRoomAdapter
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.constants.Constants.Companion.FORMID
import com.rentreadychecklist.constants.Constants.Companion.livingRoomFixList
import com.rentreadychecklist.databinding.FragmentLivingRoomFormBinding
import com.rentreadychecklist.formDataSave.FormItemList
import com.rentreadychecklist.formDataSave.FormsDummyData
import com.rentreadychecklist.model.livingRoom.LivingRoom
import com.rentreadychecklist.utils.Helper
import com.rentreadychecklist.viewmodel.AppViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * This class used for show ,edit and save LivingRoom Form Data.
 */
class LivingRoomFormFragment : Fragment() {

    lateinit var adapter: LivingRoomAdapter
    lateinit var viewBinding: FragmentLivingRoomFormBinding
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        viewBinding = FragmentLivingRoomFormBinding.inflate(inflater, container, false)
        val linearLayout = LinearLayoutManager(context)
        viewBinding.recyclerView.layoutManager = linearLayout
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // update viewModel/Database livingRoom data on click of next button
        viewBinding.next.setOnClickListener {
            Helper.hideKeyBoard(view)
            if (FormsDummyData.livingRoomData.isNotEmpty()) {
                setDataToFix()
                setOkImages()
                viewModel.updateLivingRoom(FormsDummyData.livingRoomData, FORMID)
            }

            try {
                findNavController()
                    .navigate(R.id.action_livingRoomFormFragment_to_greatRoomFormFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewBinding.previous.setOnClickListener {
            Helper.hideKeyBoard(view)
            try {
                findNavController()
                    .navigate(R.id.action_livingRoomFormFragment_to_laundryRoomFormFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewBinding.recyclerView.onFocusChangeListener =
            View.OnFocusChangeListener { v, _ ->
                Helper.hideKeyBoard(v)
            }

        setDataOnAdapter()
    }

    // set adapter as per the saved data in viewModel/database livingRoom
    fun setDataOnAdapter() {
        try {
            if (FormsDummyData.livingRoomData.isNotEmpty()) {
                setFixDataToField(FormsDummyData.livingRoomData)
                getOkImages(FormsDummyData.livingRoomData)

                adapter = LivingRoomAdapter(
                    requireContext(),
                    FormItemList.livingRoomItems,
                    FormsDummyData.livingRoomData
                )
                MainScope().launch {
                    viewBinding.recyclerView
                        .setItemViewCacheSize(FormItemList.livingRoomItems.size)
                    viewBinding.recyclerView.adapter = adapter
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    //  save  fix data in living Room FixList
    fun setFixDataToField(livingRoomData: List<LivingRoom>) {
        livingRoomFixList.clear()
        for (element in livingRoomData) {
            livingRoomFixList.add(element.fix)
        }
    }

    // set data in fix if already data saved in database fix item in LivingRoom fix list

    fun setDataToFix() {
        for (i in 0 until FormsDummyData.livingRoomData.size) {
            if (FormsDummyData.livingRoomData[i].fix.fix == getString(R.string.itemFIX)) {
                FormsDummyData.livingRoomData[i].fix = livingRoomFixList[i]
            }
        }
    }

    // set data in ok  if already data saved in database ok item in LivingRoom list
    private fun setOkImages() {
        for (i in 0 until FormsDummyData.livingRoomData.size) {
            if (FormsDummyData.livingRoomData[i].ok.ok == getString(R.string.itemOK)) {
                FormsDummyData.livingRoomData[i].ok = Constants.livingOkImageList[i]
            }
        }
    }

    // save ok item data in LivingRoom ok image list
    private fun getOkImages(livingData: List<LivingRoom>) {
        Constants.livingOkImageList.clear()
        for (element in livingData) {
            Constants.livingOkImageList.add(element.ok)
        }
    }


}