package com.example.listerpros.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.listerpros.R
import com.example.listerpros.databinding.FragmentAddNotesBinding
import com.example.listerpros.model.addnote.ResponseJobNotes
import com.example.listerpros.model.addnotesmodified.ResponseAddNoteModified
import com.example.listerpros.utils.ProgressBarDialog
import com.example.listerpros.viewmodel.JobNotesViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddNotesFragment : Fragment() {
    private lateinit var binding: FragmentAddNotesBinding
    private lateinit var progressDialog: ProgressBarDialog
    private lateinit var viewModel: JobNotesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddNotesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[JobNotesViewModel::class.java]
        progressDialog = ProgressBarDialog(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val noteId = requireArguments().getInt("noteId").toString()
        val jobId = requireArguments().getString("noteJobId").toString()

        getNoteId(noteId)

        addNotesModifiedObserver()        // observer to modify notes

        jobNotesObserver()   // Observer to Add  new Notes

        binding.submit.setOnClickListener()
        {
            progressDialog.showProgressBar()

            if (noteId > "0") {

                setNoteIdToViewModel(jobId, noteId)
            } else {
                setNoteToViewModel()
            }
        }
    }

    private fun getNoteId(noteId: String) {
        val addNotes = requireArguments().getString("notes").toString()
        if (noteId > "0") {
            binding.addNoteEdit.setText(addNotes)
        } else {
            binding.addNoteEdit.setText(resources.getString(R.string.addnotes))
        }
        val title = requireArguments().getString("title").toString()
        binding.title.text = title
    }

    private fun setNoteIdToViewModel(jobId: String, noteId: String) {
        val addNotesDone = binding.addNoteEdit.text.toString()
        viewModel.addNotesModified(jobId, addNotesDone, noteId)
    }

    private fun setNoteToViewModel() {
        val jobId = requireArguments().getString("jobId").toString()
        val addNotes = binding.addNoteEdit.text.toString()
        viewModel.jobNotes(jobId, addNotes)
    }

    //Observer
    private fun jobNotesObserver() {
        viewModel.responseJobNotes.observe(viewLifecycleOwner, Observer { call ->
            call?.enqueue(object : Callback<ResponseJobNotes> {
                override fun onResponse(
                    call: Call<ResponseJobNotes>,
                    response: Response<ResponseJobNotes>,
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            context,
                            resources.getString(R.string.noteAdded),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialog.hideProgressBar()
                        if (requireActivity().onBackPressedDispatcher.hasEnabledCallbacks()) {
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }

                    } else {

                        Toast.makeText(
                            context,
                            resources.getString(R.string.tryAgain),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialog.hideProgressBar()
                    }
                }

                override fun onFailure(call: Call<ResponseJobNotes>, t: Throwable) {

                    Toast.makeText(
                        context,
                        resources.getString(R.string.noInternet),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressDialog.hideProgressBar()
                }
            })
        })
    }


    private fun addNotesModifiedObserver() {
        viewModel.responseaddNotesModified.observe(viewLifecycleOwner, Observer { call ->
            call?.enqueue(object : Callback<ResponseAddNoteModified> {
                override fun onResponse(
                    call: Call<ResponseAddNoteModified>,
                    response: Response<ResponseAddNoteModified>,
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            context,
                            resources.getString(R.string.notemodified),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialog.hideProgressBar()
                        if (requireActivity().onBackPressedDispatcher.hasEnabledCallbacks()) {
                            requireActivity().onBackPressedDispatcher.onBackPressed()
                        }

                    } else {

                        Toast.makeText(
                            context,
                            resources.getString(R.string.tryAgain),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialog.hideProgressBar()
                    }
                }

                override fun onFailure(call: Call<ResponseAddNoteModified>, t: Throwable) {

                    Toast.makeText(
                        context,
                        resources.getString(R.string.noInternet),
                        Toast.LENGTH_SHORT
                    ).show()
                    progressDialog.hideProgressBar()
                }
            })

        })

    }
}