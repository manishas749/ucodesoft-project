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
import com.rentreadychecklist.adapters.MiscellaneousAdapter
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.constants.Constants.Companion.miscellaneousFixList
import com.rentreadychecklist.databinding.FragmentMiscellaneousFromBinding
import com.rentreadychecklist.formDataSave.FormItemList
import com.rentreadychecklist.formDataSave.FormsDummyData
import com.rentreadychecklist.model.miscellaneous.Miscellaneous
import com.rentreadychecklist.utils.Helper
import com.rentreadychecklist.viewmodel.AppViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * This class used for show ,edit and save Miscellaneous Form Data.
 */
class MiscellaneousFromFragment : Fragment() {

    lateinit var viewBinding: FragmentMiscellaneousFromBinding
    lateinit var adapter: MiscellaneousAdapter
    private lateinit var viewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        viewBinding = FragmentMiscellaneousFromBinding.inflate(
            inflater, container,
            false
        )
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        val linearLayout = LinearLayoutManager(context)
        viewBinding.recyclerView.layoutManager = linearLayout
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // update viewModel/Database miscellaneous data on click of next button
        viewBinding.next.setOnClickListener {
            Helper.hideKeyBoard(view)
            if (FormsDummyData.miscellaneousData.isNotEmpty()) {
                setDataToFix()
                setOkImages()
                viewModel.updateMiscellaneous(FormsDummyData.miscellaneousData, Constants.FORMID)
            }

            try {
                findNavController()
                    .navigate(R.id.action_miscellaneousFromFragment_to_numberOfBedroomsFragment)
            } catch (e: Exception) {
                e.printStackTrace()

            }
        }

        viewBinding.previous.setOnClickListener {
            Helper.hideKeyBoard(view)
            try {
                findNavController()
                    .navigate(R.id.action_miscellaneousFromFragment_to_kitchenFormFragment)
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

    // set adapter as per the saved data in viewModel/database miscellaneous
    fun setDataOnAdapter() {
        try {
            if (FormsDummyData.miscellaneousData.isNotEmpty()) {
                setFixDataToField(FormsDummyData.miscellaneousData)
                getOkImages(FormsDummyData.miscellaneousData)

                adapter = MiscellaneousAdapter(
                    requireContext(),
                    FormItemList.mis,
                    FormsDummyData.miscellaneousData
                )
                MainScope().launch {
                    viewBinding.recyclerView
                        .setItemViewCacheSize(FormItemList.miscellaneousItems.size)
                    viewBinding.recyclerView.adapter = adapter
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //  save  fix data in miscellaneous FixList
    fun setFixDataToField(miscellaneousData: List<Miscellaneous>) {
        miscellaneousFixList.clear()
        for (element in miscellaneousData) {
            miscellaneousFixList.add(element.fix)
        }
    }

    // set data in fix if already data saved in database fix item in miscellaneous fix list
    fun setDataToFix() {
        for (i in 0 until FormsDummyData.miscellaneousData.size) {
            if (FormsDummyData.miscellaneousData[i].fix.fix == getString(R.string.itemFIX)) {
                FormsDummyData.miscellaneousData[i].fix = miscellaneousFixList[i]
            }
        }
    }

    // set data in ok  if already data saved in database ok item in miscellaneous list
    private fun setOkImages() {
        for (i in 0 until FormsDummyData.miscellaneousData.size) {
            if (FormsDummyData.miscellaneousData[i].ok.ok == getString(R.string.itemOK)) {
                FormsDummyData.miscellaneousData[i].ok = Constants.miscellaneousOkImageList[i]
            }
        }
    }

    // save ok item data in miscellaneous ok image list
    private fun getOkImages(miscellaneousData: List<Miscellaneous>) {
        Constants.miscellaneousOkImageList.clear()
        for (element in miscellaneousData) {
            Constants.miscellaneousOkImageList.add(element.ok)
        }
    }


}