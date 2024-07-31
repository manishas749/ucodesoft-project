package com.rentreadychecklist.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.rentreadychecklist.R
import com.rentreadychecklist.databinding.FragmentEmailBinding
import com.rentreadychecklist.pdf.PdfGenerator
import com.rentreadychecklist.preferences.SharedPreference
import com.rentreadychecklist.utils.CheckPermission
import com.rentreadychecklist.utils.Helper
import com.rentreadychecklist.utils.NetworkCheck
import com.rentreadychecklist.utils.ProgressBarDialogPdf
import com.rentreadychecklist.viewmodel.AppViewModel
import java.io.File

/**
 * This class used for send PDF using Email.
 */
class EmailFragment : Fragment() {
    private lateinit var viewBinding: FragmentEmailBinding
    private lateinit var viewModel: AppViewModel
    private lateinit var progressBarDialog: ProgressBarDialogPdf
    private lateinit var networkCheck: NetworkCheck
    private var emailTo: String? = null
    private lateinit var sharedPreference: SharedPreference
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var navController: FragmentContainerView
    private lateinit var checkPermission: CheckPermission

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentEmailBinding.inflate(inflater, container, false)
        navController = requireActivity().findViewById(R.id.fragmentContainerView)
        checkCustomToolbarVisibility()
        progressBarDialog = ProgressBarDialogPdf(requireContext())
        networkCheck = NetworkCheck(requireContext())
        sharedPreference = SharedPreference(requireContext())
        sharedPreference.loadEmail()
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        checkPermission = CheckPermission(requireContext())
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val autoSuggestionEmail = sharedPreference.emailList.distinct()
        arrayAdapter = ArrayAdapter<String>(
            requireContext(), android.R.layout.simple_list_item_1,
            autoSuggestionEmail.takeLast(5)
        )
        viewBinding.emailTextview.setAdapter(arrayAdapter)    // to set last 5 emails in textview autosuggestion

        viewBinding.submit.setOnClickListener {
            emailTo = viewBinding.emailTextview.text.toString()
            validateEmail(emailTo!!)
        }

        viewBinding.backPress.setOnClickListener {
            if (requireActivity().onBackPressedDispatcher.hasEnabledCallbacks()) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        viewBinding.linearLayout.setOnClickListener {
            Helper.hideKeyBoard(view)
        }
    }

    override fun onStart() {
        super.onStart()
        checkBuildVersionAndPermission()
    }

    private fun checkBuildVersionAndPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                checkPermission.showStoragePermissionDialog()
            } else {
                generatePdf()
            }
        } else {
            generatePdf()
        }
    }

    // to generate PDF on the email page

    private fun generatePdf() {
        progressBarDialog.showProgressBar()
        viewModel.readFormDataID.observe(viewLifecycleOwner)
        {
            try {
                PdfGenerator(requireContext()).apply {
                    pdfFileInPrivate(it, progressBarDialog)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // validation of the entered email

    private fun validateEmail(emailTo: String) {

        viewBinding.emailTextview.error = null
        if (TextUtils.isEmpty(emailTo)) {
            viewBinding.emailTextview.error = resources.getString(R.string.enterEmailAddress)
        } else if (!Helper.isValidEmail(emailTo) || !Helper.isValidEmailFormat(emailTo)) {
            viewBinding.emailTextview.error = resources.getString(R.string.invalidEmail)
        } else {
            viewBinding.emailTextview.error = null
            sharedPreference.emailList.add(viewBinding.emailTextview.text.toString())
            sharedPreference.saveEmail(sharedPreference.emailList)
            val networkCheck = networkCheck.checkNetwork()
            if (networkCheck) {
                sendEmail1()
            } else {
                Toast.makeText(
                    context,
                    resources.getString(R.string.noInternet),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    // email generation code

    private fun sendEmail1() {
        val fileName = context?.resources?.getString(R.string.rentReadyPdf)
        val folderName = context?.resources?.getString(R.string.rentReadyFolder)
        val path: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            (context as Activity).externalMediaDirs.firstOrNull()!!.path
        } else {
            Environment.getExternalStorageDirectory().toString() + folderName
        }
        val file = File(path, fileName.toString())
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = context?.resources?.getString(R.string.emailIntentTypeDir)
        emailIntent.type = context?.resources?.getString(R.string.emailIntentTypePdf)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailTo))
        emailIntent.putExtra(
            Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                requireContext(),
                context?.resources?.getString(R.string.com_rentreadychecklist_provider).toString(),
                file
            )
        )
        emailIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            context?.resources?.getString(R.string.rent_ready_pdf)
        )
        emailIntent.putExtra(
            Intent.EXTRA_TEXT, context?.resources?.getString(R.string.hello) +
                    "\n" +
                    context?.resources?.getString(R.string.emailAttachment)
        )
        startActivity(
            Intent.createChooser(
                emailIntent,
                context?.resources?.getString(R.string.sendingEmail)
            )
        )
        if (navController.findNavController().currentDestination?.id == R.id.navigationDrawerFragment) {
            navController.findNavController()
                .navigate(R.id.action_navigationDrawerFragment_to_homeFragment)
        } else {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }

    }

    private fun checkCustomToolbarVisibility() {
        if (navController.findNavController().currentDestination?.id != R.id.navigationDrawerFragment) {
            navController.findNavController()
            viewBinding.customToolbar.visibility = View.VISIBLE
        }
    }

}