package com.example.listerpros.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.listerpros.R
import com.example.listerpros.databinding.FragmentSkipNoteBinding
import com.example.listerpros.model.updatetaskstatus.UpdateTaskStatus
import com.example.listerpros.utils.ProgressBarDialog
import com.example.listerpros.viewmodel.UpdateTaskStatusViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SkipNoteFragment : Fragment() {

    lateinit var binding: FragmentSkipNoteBinding
    private lateinit var progressDialog: ProgressBarDialog
    private lateinit var viewModel: UpdateTaskStatusViewModel
    private val skipped = "SKIPPED"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSkipNoteBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[UpdateTaskStatusViewModel::class.java]
        progressDialog = ProgressBarDialog(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskId = requireArguments().getInt("taskId").toString()
        val taskName = requireArguments().getString("taskName")

        binding.title.text = taskName.toString()

        binding.submit.setOnClickListener {
            updateTaskStatus(taskId)
        }

        updateTaskStatusObserver()

    }

    private fun updateTaskStatus(taskId: String) {
        val skipNote = binding.addNoteEdit.text?.trim()
        Log.d("idTask", taskId)
        if (skipNote!!.isNotEmpty()) {
            viewModel.updateTaskStatus(taskId, skipNote.toString(), skipped)
        } else {
            Toast.makeText(
                context,
                resources.getString(R.string.pleaseEnterSomthing),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateTaskStatusObserver() {
        viewModel.responseUpdateTaskStatus.observe(viewLifecycleOwner, Observer { call ->
            call?.enqueue(object : Callback<UpdateTaskStatus> {
                override fun onResponse(
                    call: Call<UpdateTaskStatus>,
                    response: Response<UpdateTaskStatus>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show()
                        if (requireActivity().onBackPressedDispatcher.hasEnabledCallbacks()) {
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }
                    } else {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<UpdateTaskStatus>, t: Throwable) {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.tryAgain),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            })
        })
    }

}