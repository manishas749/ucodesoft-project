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
import com.rentreadychecklist.adapters.OutsideFormAdapter
import com.rentreadychecklist.constants.Constants.Companion.FORMID
import com.rentreadychecklist.constants.Constants.Companion.fixList
import com.rentreadychecklist.constants.Constants.Companion.outsideOkImageList
import com.rentreadychecklist.databinding.FragmentOutsideFormBinding
import com.rentreadychecklist.formDataSave.FormItemList
import com.rentreadychecklist.formDataSave.FormsDummyData
import com.rentreadychecklist.model.outside.Outside
import com.rentreadychecklist.utils.Helper
import com.rentreadychecklist.viewmodel.AppViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * This class used for show ,edit and save Outside Form Data.
 */
class OutsideFormFragment : Fragment() {

    lateinit var viewBinding: FragmentOutsideFormBinding
    lateinit var adapter: OutsideFormAdapter
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentOutsideFormBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        val linearLayout = LinearLayoutManager(context)
        viewBinding.recyclerView.layoutManager = linearLayout
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.previous.setOnClickListener {
            Helper.hideKeyBoard(view)
            if (requireActivity().onBackPressedDispatcher.hasEnabledCallbacks()) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }


        // update outside database data  on the click of next

        viewBinding.next.setOnClickListener {
            Helper.hideKeyBoard(view)
            if (FormsDummyData.outsideData.isNotEmpty()) {
                setDataToFix()
                setOkImages()
                viewModel.updateOutside(FormsDummyData.outsideData, FORMID)
            }

            try {
                view.findNavController()
                    .navigate(R.id.action_outsideFormFragment_to_frontDoorsFormFragment2)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        setDataOnAdapter()
    }

    // set data in adapter which is already saved in database outside
    fun setDataOnAdapter() {
        try {
            if (FormsDummyData.outsideData.isNotEmpty()) {
            setFixDataToField(FormsDummyData.outsideData)
            getOkImages(FormsDummyData.outsideData)
                adapter = OutsideFormAdapter(
                    requireContext(),
                    FormItemList.outsideItems,
                    FormsDummyData.outsideData
                )
                MainScope().launch {
                    viewBinding.recyclerView.setItemViewCacheSize(FormItemList.outsideItems.size)
                    viewBinding.recyclerView.adapter = adapter
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //save  fix data in outside fixList

    fun setFixDataToField(outsideData: List<Outside>) {
        fixList.clear()
        for (element in outsideData) {
            fixList.add(element.fix)
        }
    }

    // set data in fix if already data saved in database fix item in outside fix list

    fun setDataToFix() {
        for (i in 0 until FormsDummyData.outsideData.size) {
            if (FormsDummyData.outsideData[i].fix.fix == getString(R.string.itemFIX)) {
                FormsDummyData.outsideData[i].fix = fixList[i]
            }
        }
    }


    // set data in ok  if already data saved in database ok item in outside  list
    private fun setOkImages() {
        for (i in 0 until FormsDummyData.outsideData.size) {
            if (FormsDummyData.outsideData[i].ok.ok == getString(R.string.itemOK)) {
                FormsDummyData.outsideData[i].ok = outsideOkImageList[i]
            }
        }
    }


    // save ok item data in outside ok image list
    private fun getOkImages(outsideData: List<Outside>) {
        outsideOkImageList.clear()
        for (element in outsideData) {
            outsideOkImageList.add(element.ok)
        }
    }
}