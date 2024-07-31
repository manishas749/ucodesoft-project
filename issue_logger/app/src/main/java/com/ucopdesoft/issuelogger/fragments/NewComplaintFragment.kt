package com.ucopdesoft.issuelogger.fragments

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.camerautil.ImagePicker
import com.example.camerautil.listener.ChooserClickListener
import com.ucopdesoft.issuelogger.R
import com.ucopdesoft.issuelogger.adapters.ImageAdapter
import com.ucopdesoft.issuelogger.databinding.FragmentNewComplaintBinding
import com.ucopdesoft.issuelogger.listeners.CheckImageListListener
import com.ucopdesoft.issuelogger.login__authentication.ComplaintListCallBack
import com.ucopdesoft.issuelogger.utils.CheckPermission
import com.ucopdesoft.issuelogger.utils.ToastMessage
import com.ucopdesoft.issuelogger.viewmodels.ComplaintsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class NewComplaintFragment : Fragment(), ComplaintListCallBack, ChooserClickListener,
    CheckImageListListener {

    companion object {
        var REQ_CODE = 0
        var imageUri: Uri? = null
    }

    private lateinit var binding: FragmentNewComplaintBinding
    private lateinit var complaintViewModel: ComplaintsViewModel
    private lateinit var imagePicker: ImagePicker.Builder
    private lateinit var checkPermission: CheckPermission
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var adapter: ImageAdapter
    private val liveImageList =
        MutableLiveData<MutableList<Uri>>()


    private val imageUriList = ArrayList<Uri>()
    private var longitude: Double = 0.0
    private var latitude: Double = 0.0

    private val location = MutableLiveData("")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_complaint, container, false)
        requireActivity().window.apply {
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
            navigationBarColor = ContextCompat.getColor(requireContext(), R.color.bg_color)
        }
        complaintViewModel = ViewModelProvider(this)[ComplaintsViewModel::class.java]
        checkPermission = CheckPermission(requireContext())
        activityResultLauncher(requireContext())


        imagePicker = ImagePicker.with(fragment = this)

        binding.imgRecView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        location.observe(viewLifecycleOwner) {
            binding.locationTv.text = it
        }
        binding.backPress.setOnClickListener {
            if (requireActivity().onBackPressedDispatcher.hasEnabledCallbacks()) {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        complaintViewModel.getCurrentLocation(requireContext(), this@NewComplaintFragment)

        binding.apply {

            currentTimeTv.text = complaintViewModel.getTime()

            complaintViewModel.statusMessage.observe(viewLifecycleOwner) {
                if (it) {
                    ToastMessage.showMessage(
                        requireContext(),
                        resources.getString(R.string.complaint_register_successfully)
                    )
                    //clearData()
                    findNavController().popBackStack()
                } else {
                    binding.processingLayout.visibility = View.GONE
                    binding.complaintRegisterLayout.visibility = View.VISIBLE
                    ToastMessage.showMessage(
                        requireContext(),
                        resources.getString(R.string.unable_to_register_please_try_again_later)
                    )
                }
            }

            complaintViewModel.counter.observe(viewLifecycleOwner) {
                if (it.second == imageUriList.size || it.second == 1) {
                    complaintViewModel.setImagesUris(imageUriList)
                    complaintViewModel.registerComplaint(requireContext(), it.first)
                }
            }

            liveImageList.observe(viewLifecycleOwner) {
                imgRecView.adapter = ImageAdapter(
                    it,
                    this@NewComplaintFragment
                )
                if (it.isNotEmpty()) {
                    imgRecView.visibility = View.VISIBLE
                    addImageText.visibility = View.GONE

                } else {
                    imgRecView.visibility = View.GONE
                    addImageText.visibility = View.VISIBLE

                }
            }

            uploadImageBtn.setOnClickListener {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    cameraAndGallerySelectorDialog(requireContext())
                } else {
                    checkPermission.showCameraAndGalleryPermissionDialog()
                }

            }



            submitBtn.setOnClickListener {
                val title = complaintTitleTv.text.toString().trim()
                val description = complaintDescriptionEt.text.toString().trim()
                val address = locationTv.text.toString().trim()
                val date = currentTimeTv.text.toString().trim()

                if (TextUtils.isEmpty(title)) {

                    ToastMessage.showMessage(
                        requireContext(),
                        resources.getString(R.string.please_enter_title_or_description)
                    )
                    binding.complaintTitleTv.error = "Please enter the issue"

                } else if (!binding.checkBox.isChecked) {
                    ToastMessage.showMessage(
                        requireContext(),
                        resources.getString(R.string.please_acknowlegdge)
                    )
                } else {

                    binding.complaintTitleTv.error = null
                    complaintViewModel.setDescriptionURI(
                        description,
                        imageUriList,
                        title,
                        address,
                        latitude,
                        longitude,
                        date
                    )
                    complaintRegisterLayout.visibility = View.GONE
                    processingLayout.visibility = View.VISIBLE
                    complaintViewModel.registerComplaint(requireContext())


                    complaintViewModel.setDescriptionURI(
                        description,
                        imageUriList,
                        title,
                        address,
                        latitude,
                        longitude,
                        date
                    )
                    complaintRegisterLayout.visibility = View.GONE
                    processingLayout.visibility = View.VISIBLE
                    complaintViewModel.registerComplaint(requireContext())
                }
            }
        }
    }


    override fun onResponseLocation(address: MutableList<Address>) {
        location.postValue(address[0].getAddressLine(0))
        longitude = address[0].longitude
        latitude = address[0].latitude
    }


    private fun cameraAndGallerySelectorDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.upload_image_dialog_layout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val takePhotoBtn = dialog.findViewById<Button>(R.id.takePhotoBtn)
        val galleryBtn = dialog.findViewById<Button>(R.id.chooseFromGalleryBtn)
        val cancelBtn = dialog.findViewById<Button>(R.id.cancelBtn)

        takePhotoBtn.setOnClickListener {
            dialog.dismiss()
            val values = ContentValues()
            values.put(
                MediaStore.Images.Media.TITLE,
                context.resources.getString(R.string.newPicture)
            )
            values.put(
                MediaStore.Images.Media.DESCRIPTION,
                context.resources.getString(R.string.fromYourCamera)
            )
            imageUri = context.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
            )
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            takePhotoLauncher.launch(cameraIntent)
        }

        galleryBtn.setOnClickListener {
            dialog.dismiss()
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = context.resources.getString(R.string.imageType)
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            galleryLauncher.launch(galleryIntent)
        }

        cancelBtn.setOnClickListener { dialog.dismiss() }

        dialog.show()
    }

    private fun activityResultLauncher(context: Context) {
        takePhotoLauncher = registerForActivityResult(
            ActivityResultContracts
                .StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (imageUri != null) {
                    try {
                        saveImage(imageUri!!, context)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        }

        galleryLauncher = registerForActivityResult(
            ActivityResultContracts
                .StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUriList.clear()

                if (result.data?.clipData != null) {
                    val count = result.data?.clipData?.itemCount!!
                    MainScope().launch(Dispatchers.IO) {
                        for (i in 0 until count) {
                            try {
                                val uri = result.data?.clipData?.getItemAt(i)?.uri
                                if (uri != null) {
                                    imageUriList.add(uri)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        liveImageList.postValue(imageUriList)
                    }
                } else if (result.data != null) {
                    val uri = result.data!!.data
                    if (uri != null) {
                        saveImage(uri, context)
                    }

                }
            }
        }
    }

    private fun saveImage(imageUri: Uri, context: Context) {
        imageUriList.add(imageUri)
        liveImageList.postValue(imageUriList)
    }


    override fun checkListImages() {
        binding.imgRecView.visibility = View.GONE
        binding.uploadImageBtn.visibility = View.VISIBLE
        binding.addImageText.visibility = View.VISIBLE
    }

}