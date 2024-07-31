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
import com.rentreadychecklist.adapters.DiningRoomFormAdapter
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.constants.Constants.Companion.FORMID
import com.rentreadychecklist.constants.Constants.Companion.diningRoomFixList
import com.rentreadychecklist.databinding.FragmentDiningRoomFormBinding
import com.rentreadychecklist.formDataSave.FormItemList
import com.rentreadychecklist.formDataSave.FormsDummyData
import com.rentreadychecklist.model.diningRoom.DiningRoom
import com.rentreadychecklist.utils.Helper
import com.rentreadychecklist.viewmodel.AppViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * This class used for show ,edit and save DiningRoom Form Data.
 */
class DiningRoomFormFragment : Fragment() {

    lateinit var adapter: DiningRoomFormAdapter
    lateinit var viewBinding: FragmentDiningRoomFormBinding
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentDiningRoomFormBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        val linearLayout = LinearLayoutManager(context)
        viewBinding.recyclerView.layoutManager = linearLayout
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // update dining room database data  on the click of next
        viewBinding.next.setOnClickListener {
            Helper.hideKeyBoard(view)
            if (FormsDummyData.diningRoomData.isNotEmpty()) {
                setDataToFix()
                setOkImages()
                viewModel.updateDiningRoom(FormsDummyData.diningRoomData, FORMID)
            }
            try {
                findNavController().navigate(R.id.action_diningRoomFormFragment_to_kitchenFormFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        viewBinding.previous.setOnClickListener {
            Helper.hideKeyBoard(view)
            try {
                findNavController().navigate(R.id.action_diningRoomFormFragment_to_greatRoomFormFragment)
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

    // set data in adapter which is already saved in database dining room
    private fun setDataOnAdapter() {
        try {
            if (FormsDummyData.greatRoomData.isNotEmpty()) {
                setFixDataToField(FormsDummyData.diningRoomData)
                getOkImages(FormsDummyData.diningRoomData)

                adapter =
                    DiningRoomFormAdapter(
                        requireContext(),
                        FormItemList.dining,
                        FormsDummyData.diningRoomData
                    )
                MainScope().launch {
                    viewBinding.recyclerView
                        .setItemViewCacheSize(FormItemList.DiningRoomItems.size)
                    viewBinding.recyclerView.adapter = adapter
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //save  fix data in dining roomFixList
    fun setFixDataToField(diningRoomData: List<DiningRoom>) {
        diningRoomFixList.clear()
        for (element in diningRoomData) {
            diningRoomFixList.add(element.fix)
        }
    }

    // set data in fix if already data saved in database fix item in dining room fix list
    fun setDataToFix() {
        for (i in 0 until FormsDummyData.diningRoomData.size) {
            if (FormsDummyData.diningRoomData[i].fix.fix == getString(R.string.itemFIX)) {
                FormsDummyData.diningRoomData[i].fix = diningRoomFixList[i]
            }
        }
    }

    // set data in ok  if already data saved in database ok item in dining room  list
    private fun setOkImages() {
        for (i in 0 until FormsDummyData.diningRoomData.size) {
            if (FormsDummyData.diningRoomData[i].ok.ok == getString(R.string.itemOK)) {
                FormsDummyData.diningRoomData[i].ok = Constants.diningOkImageList[i]
            }
        }
    }

    // save ok item data in dining ok image list
    private fun getOkImages(diningRoomData: List<DiningRoom>) {
        Constants.diningOkImageList.clear()
        for (element in diningRoomData) {
            Constants.diningOkImageList.add(element.ok)
        }
    }

}