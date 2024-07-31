package com.rentreadychecklist.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.rentreadychecklist.R
import com.rentreadychecklist.constants.Constants.Companion.FORMID
import com.rentreadychecklist.databinding.FragmentAdditionalNotesBinding
import com.rentreadychecklist.formDataSave.FormsDummyData
import com.rentreadychecklist.model.homescreen.HomeScreen
import com.rentreadychecklist.viewmodel.AppViewModel

/**
 * This class used for add additional notes in  Checklist FORM.
 */
class AdditionalNotesFragment : Fragment() {

    lateinit var viewBinding: FragmentAdditionalNotesBinding
    lateinit var viewModel: AppViewModel
    private lateinit var pdfConfirmDialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentAdditionalNotesBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        getNewChecklistData()
        pdfConfirmationDialogInit()
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.next.setOnClickListener {
            pdfConfirmationDialog()
        }

        viewBinding.previous.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_additionalNotesFragment_to_bathroomFormFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // pdf confirmation dialog whether want to continue writing or generate PDF
    // if clicked on yes saved the additional notes in database

    private fun pdfConfirmationDialogInit() {
        pdfConfirmDialog = Dialog(requireContext())
        pdfConfirmDialog.setContentView(R.layout.pdf_confirmation_dialog)
        pdfConfirmDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pdfConfirmDialog.setCancelable(false)
    }

    private fun pdfConfirmationDialog() {
        val yesButton = pdfConfirmDialog.findViewById<Button>(R.id.yes)
        val noButton = pdfConfirmDialog.findViewById<Button>(R.id.no)

        if (!pdfConfirmDialog.isShowing) {
            pdfConfirmDialog.show()
        }
        yesButton.setOnClickListener {
            pdfConfirmDialog.dismiss()
            if (FormsDummyData.homeScreenData.isNotEmpty()) {
                FormsDummyData.homeScreenData[0].additionalNotes =
                    viewBinding.additionalNotesET.text.toString()
                viewModel.updateHomeScreen(FormsDummyData.homeScreenData, FORMID)
            }


            try {
                findNavController().navigate(R.id.action_additionalNotesFragment_to_emailFragment)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        noButton.setOnClickListener {
            pdfConfirmDialog.dismiss()
        }
    }

    // To set the additional notes from  database if already saved

    private fun getNewChecklistData() {
        try {
            if (FormsDummyData.homeScreenData.isNotEmpty()) {
                viewBinding.additionalNotesET.setText(FormsDummyData.homeScreenData[0].additionalNotes)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}