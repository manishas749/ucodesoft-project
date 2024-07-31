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
import com.rentreadychecklist.adapters.KitchenFormAdapter
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.constants.Constants.Companion.FORMID
import com.rentreadychecklist.constants.Constants.Companion.kitchenFixList
import com.rentreadychecklist.databinding.FragmentKitchenFormBinding
import com.rentreadychecklist.formDataSave.FormItemList
import com.rentreadychecklist.formDataSave.FormsDummyData
import com.rentreadychecklist.model.kitchen.Kitchen
import com.rentreadychecklist.utils.Helper
import com.rentreadychecklist.viewmodel.AppViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * This class used for show ,edit and save Kitchen Form Data.
 */
class KitchenFormFragment : Fragment() {

    lateinit var adapter: KitchenFormAdapter
    lateinit var viewBinding: FragmentKitchenFormBinding
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentKitchenFormBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        val linearLayout = LinearLayoutManager(context)
        viewBinding.recyclerView.layoutManager = linearLayout
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // update viewModel/Database kitchen data on click of next button

        viewBinding.next.setOnClickListener {
            Helper.hideKeyBoard(view)
            if (FormsDummyData.kitchenData.isNotEmpty()) {
                setDataToFix()
                setOkImages()
                viewModel.updateKitchen(FormsDummyData.kitchenData, FORMID)
                try {
                    findNavController()
                        .navigate(R.id.action_kitchenFormFragment_to_miscellaneousFromFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        viewBinding.previous.setOnClickListener {
            Helper.hideKeyBoard(view)
            try {
                findNavController()
                    .navigate(R.id.action_kitchenFormFragment_to_diningRoomFormFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        setDataOnAdapter()
    }

    // set adapter as per the saved data in viewModel/database kitchen
    private fun setDataOnAdapter() {
        try {
            if (FormsDummyData.kitchenData.isNotEmpty()) {
                setFixDataToField(FormsDummyData.kitchenData)
                getOkImages(FormsDummyData.kitchenData)

                adapter =
                    KitchenFormAdapter(
                        requireContext(),
                        FormItemList.kitchenItems,
                        FormsDummyData.kitchenData
                    )
                MainScope().launch {
                    viewBinding.recyclerView
                        .setItemViewCacheSize(FormItemList.kitchenItems.size)
                    viewBinding.recyclerView.adapter = adapter
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //  save  fix data in kitchen FixList
    fun setFixDataToField(kitchenData: List<Kitchen>) {
        kitchenFixList.clear()
        for (element in kitchenData) {
            kitchenFixList.add(element.fix)
        }
    }

    // set data in fix if already data saved in database fix item in kitchen fix list
    fun setDataToFix() {
        for (i in 0 until FormsDummyData.kitchenData.size) {
            if (FormsDummyData.kitchenData[i].fix.fix == getString(R.string.itemFIX)) {
                FormsDummyData.kitchenData[i].fix = kitchenFixList[i]
            }
        }
    }

    // set data in ok  if already data saved in database ok item in kitchen list
    private fun setOkImages() {
        for (i in 0 until FormsDummyData.kitchenData.size) {
            if (FormsDummyData.kitchenData[i].ok.ok == getString(R.string.itemOK)) {
                FormsDummyData.kitchenData[i].ok = Constants.kitchenOkImageList[i]
            }
        }
    }

    // save ok item data in kitchen ok image list
    private fun getOkImages(kitchenData: List<Kitchen>) {
        Constants.kitchenOkImageList.clear()
        for (element in kitchenData) {
            Constants.kitchenOkImageList.add(element.ok)
        }
    }

}