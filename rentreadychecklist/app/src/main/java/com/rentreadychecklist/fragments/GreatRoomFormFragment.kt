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
import com.rentreadychecklist.adapters.GreatRoomAdapter
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.constants.Constants.Companion.FORMID
import com.rentreadychecklist.constants.Constants.Companion.greatRoomFixList
import com.rentreadychecklist.databinding.FragmentGreatRoomFormBinding
import com.rentreadychecklist.formDataSave.FormItemList
import com.rentreadychecklist.formDataSave.FormsDummyData
import com.rentreadychecklist.model.greatroom.GreatRoom
import com.rentreadychecklist.utils.Helper
import com.rentreadychecklist.viewmodel.AppViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * This class used for show ,edit and save GreatRoom Form Data.
 */
class GreatRoomFormFragment : Fragment() {

    lateinit var adapter: GreatRoomAdapter
    lateinit var viewBinding: FragmentGreatRoomFormBinding
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentGreatRoomFormBinding.inflate(inflater, container, false)
        val linearLayout = LinearLayoutManager(context)
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        viewBinding.recyclerView.layoutManager = linearLayout
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // update viewModel/Database greatRoom data on click of next button

        viewBinding.next.setOnClickListener {
            Helper.hideKeyBoard(view)
            if (FormsDummyData.greatRoomData.isNotEmpty()) {
                setDataToFix()
                setOkImages()
                viewModel.updateGreatRoom(FormsDummyData.greatRoomData, FORMID)
            }
            try {
                findNavController()
                    .navigate(R.id.action_greatRoomFormFragment_to_diningRoomFormFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewBinding.previous.setOnClickListener {
            Helper.hideKeyBoard(view)
            try {
                findNavController()
                    .navigate(R.id.action_greatRoomFormFragment_to_livingRoomFormFragment)
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

    // set adapter as per the saved data in viewModel/database greatRoom

    private fun setDataOnAdapter() {
        try {
            if (FormsDummyData.greatRoomData.isNotEmpty()) {
                setFixDataToField(FormsDummyData.greatRoomData)
                getOkImages(FormsDummyData.greatRoomData)

                adapter =
                    GreatRoomAdapter(
                        requireContext(),
                        FormItemList.greatRoomItems,
                        FormsDummyData.greatRoomData
                    )
                MainScope().launch {
                    viewBinding.recyclerView
                        .setItemViewCacheSize(FormItemList.greatRoomItems.size)
                    viewBinding.recyclerView.adapter = adapter
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //  save  fix data in greatRoom FixList

    fun setFixDataToField(greatRoomData: List<GreatRoom>) {
        greatRoomFixList.clear()
        for (element in greatRoomData) {
            greatRoomFixList.add(element.fix)
        }
    }

    // set data in fix if already data saved in database fix item in greatRoom fix list
    fun setDataToFix() {
        for (i in 0 until FormsDummyData.greatRoomData.size) {
            if (FormsDummyData.greatRoomData[i].fix.fix == getString(R.string.itemFIX)) {
                FormsDummyData.greatRoomData[i].fix = greatRoomFixList[i]
            }
        }
    }


    // set data in ok  if already data saved in database ok item in greatRoom list
    private fun setOkImages() {
        for (i in 0 until FormsDummyData.greatRoomData.size) {
            if (FormsDummyData.greatRoomData[i].ok.ok == getString(R.string.itemOK)) {
                FormsDummyData.greatRoomData[i].ok = Constants.greatOkImageList[i]
            }
        }
    }

    // save ok item data in greatRoom ok image list
    private fun getOkImages(greatRoomData: List<GreatRoom>) {
        Constants.greatOkImageList.clear()
        for (element in greatRoomData) {
            Constants.greatOkImageList.add(element.ok)
        }
    }

}

