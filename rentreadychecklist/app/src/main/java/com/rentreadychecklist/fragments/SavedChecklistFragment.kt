package com.rentreadychecklist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rentreadychecklist.RoomUpdateInterface
import com.rentreadychecklist.adapters.SavedChecklistAdapter
import com.rentreadychecklist.databinding.FragmentSavedChecklistBinding
import com.rentreadychecklist.utils.ProgressBarDialog
import com.rentreadychecklist.viewmodel.AppViewModel

/**
 * This class used for show SavedChecklist.
 */
class SavedChecklistFragment : Fragment(), RoomUpdateInterface {

    private lateinit var viewBinding: FragmentSavedChecklistBinding
    private lateinit var viewModel: AppViewModel
    lateinit var adapter: SavedChecklistAdapter
    private lateinit var progressBar: ProgressBarDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentSavedChecklistBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        progressBar = ProgressBarDialog(requireContext())
        val linearLayout = LinearLayoutManager(requireContext())
        viewBinding.savedChecklistRecyclerView.layoutManager = linearLayout
        progressBar.showProgressBar()
        getSavedChecklist()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding.backPress.setOnClickListener {
            if (requireActivity().onBackPressedDispatcher.hasEnabledCallbacks()) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

    }

    //Set saved list from viewModel/Database to adapter.
    private fun getSavedChecklist() {
        viewModel.readFormData.observe(viewLifecycleOwner) {
            try {
                progressBar.hideProgressBar()
                val list = ArrayList(it)
                if (list.isNotEmpty()) {
                    viewBinding.savedCheckListTv.visibility = View.GONE
                    viewBinding.savedChecklistRecyclerView.visibility = View.VISIBLE
                    adapter = SavedChecklistAdapter(
                        requireContext(), list, this,
                        this@SavedChecklistFragment
                    )
                    viewBinding.savedChecklistRecyclerView.adapter = adapter
                    viewModel.readFormData.removeObservers(viewLifecycleOwner)
                } else {
                    viewBinding.savedCheckListTv.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                progressBar.hideProgressBar()
            }
        }
    }

    //Show List empty in case the list is empty.
    override fun checkSavedList() {
        super.checkSavedList()
        viewBinding.savedCheckListTv.visibility = View.VISIBLE
        viewBinding.savedChecklistRecyclerView.visibility = View.GONE
    }
}