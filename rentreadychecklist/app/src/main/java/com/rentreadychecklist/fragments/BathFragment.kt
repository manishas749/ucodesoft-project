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
import com.rentreadychecklist.adapters.BathroomChildAdapter
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.databinding.FragmentBathBinding
import com.rentreadychecklist.formDataSave.FormItemList
import com.rentreadychecklist.model.bathroom.Bathroom
import com.rentreadychecklist.model.bathroom.BathroomFix
import com.rentreadychecklist.viewmodel.AppViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class BathFragment : Fragment() {
    lateinit var viewBinding: FragmentBathBinding
    lateinit var viewModel: AppViewModel
    private lateinit var bathroomList: MutableList<Bathroom>
    private lateinit var bathChildAdapter: BathroomChildAdapter
    private var totalNumberOfBathroom = 0
    private var bathroomNumber = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentBathBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        bathroomList = mutableListOf()
        val linearLayout = LinearLayoutManager(context)
        viewBinding.recyclerView.layoutManager = linearLayout
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataOnAdapter()

        viewBinding.next.setOnClickListener {
            nextBathRoomPosition()
        }

        viewBinding.previous.setOnClickListener {
            previousBathroom()
        }

    }

    private fun nextBathRoomPosition() {
        if (bathroomNumber < totalNumberOfBathroom) {
            when (bathroomNumber) {
                1 -> {
                    setDataToAdapterWithPosition(1)
                    bathroomNumber = 2
                    viewBinding.formTitle.text = getString(R.string.bathroom_2)
                }
                2 -> {
                    setDataToAdapterWithPosition(2)
                    bathroomNumber = 3
                    viewBinding.formTitle.text = getString(R.string.bathroom_3)
                }
                3 -> {
                    setDataToAdapterWithPosition(3)
                    bathroomNumber = 4
                    viewBinding.formTitle.text = getString(R.string.bathroom_4)
                }
                4 -> {
                    setDataToAdapterWithPosition(4)
                    bathroomNumber = 5
                    viewBinding.formTitle.text = getString(R.string.bathroom_5)
                }
                5 -> {
                    setDataToAdapterWithPosition(5)
                    bathroomNumber = 6
                    viewBinding.formTitle.text = getString(R.string.bathroom_6)
                }
                6 -> {
                    if (bathroomList.isNotEmpty()) {
                        setDataToFix()
                        viewModel.updateBathroom(bathroomList, Constants.FORMID)
                    }
                    try {
                        findNavController().navigate(R.id.action_bathFragment_to_emailFragment)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        } else {
            if (bathroomList.isNotEmpty()) {
                setDataToFix()
                viewModel.updateBathroom(bathroomList, Constants.FORMID)
            }
            try {
                findNavController().navigate(R.id.action_bathFragment_to_emailFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun previousBathroom() {

        when (bathroomNumber) {
            1 -> {
                if (bathroomList.isNotEmpty()) {
                    setDataToFix()
                    viewModel.updateBathroom(bathroomList, Constants.FORMID)
                }
                try {
                    findNavController().navigate(R.id.action_bathFragment_to_numberOfBathroomsFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            2 -> {
                setDataToAdapterWithPosition(0)
                bathroomNumber = 1
                viewBinding.formTitle.text = getString(R.string.bathroom_1)
            }
            3 -> {
                setDataToAdapterWithPosition(1)
                bathroomNumber = 2
                viewBinding.formTitle.text = getString(R.string.bathroom_2)
            }
            4 -> {
                setDataToAdapterWithPosition(2)
                bathroomNumber = 3
                viewBinding.formTitle.text = getString(R.string.bathroom_3)
            }
            5 -> {
                setDataToAdapterWithPosition(3)
                bathroomNumber = 4
                viewBinding.formTitle.text = getString(R.string.bathroom_4)
            }
            6 -> {
                setDataToAdapterWithPosition(4)
                bathroomNumber = 5
                viewBinding.formTitle.text = getString(R.string.bathroom_5)
            }
        }

    }

    fun setDataOnAdapter() {
        try {
            viewModel.readFormDataID.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    if (it[0].bathroom.isNotEmpty()) {
                        setFixData(it[0].bathroom)
                        bathroomList = it[0].bathroom.toMutableList()
                        totalNumberOfBathroom = bathroomList.size
                        bathChildAdapter = BathroomChildAdapter(
                            requireContext(), FormItemList.bathroomItemsList,
                            bathroomList[0].list, 0

                        )
                        MainScope().launch {
                            viewBinding.recyclerView.setItemViewCacheSize(bathroomList[0].list.size)
                            viewBinding.recyclerView.adapter = bathChildAdapter
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setDataToAdapterWithPosition(position: Int) {
        bathChildAdapter = BathroomChildAdapter(
            requireContext(), FormItemList.bathroomItemsList,
            bathroomList[position].list, position

        )
        MainScope().launch {
            viewBinding.recyclerView.setItemViewCacheSize(bathroomList[position].list.size)
            viewBinding.recyclerView.adapter = bathChildAdapter
        }
    }

    private fun setFixData(bathroomData: List<Bathroom>) {
        Constants.bathroomFixList.clear()
        for (element in bathroomData) {
            val list = mutableListOf<BathroomFix>()
            for (j in 0 until element.list.size) {
                list.add(element.list[j].fix)
            }
            Constants.bathroomFixList.add(list)
        }
    }

    fun setDataToFix() {
        for (i in 0 until bathroomList.size) {
            for (j in 0 until bathroomList[i].list.size) {
                if (bathroomList[i].list[j].fix.fix == "fix") {
                    bathroomList[i].list[j].fix = Constants.bathroomFixList[i][j]
                }
            }
        }
    }

}