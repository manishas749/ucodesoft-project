package com.rentreadychecklist.fragments

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rentreadychecklist.R
import com.rentreadychecklist.RoomUpdateInterface
import com.rentreadychecklist.adapters.ImageAdapter
import com.rentreadychecklist.constants.Constants
import com.rentreadychecklist.constants.Constants.Companion.bathroomOkImageList
import com.rentreadychecklist.constants.Constants.Companion.bedroomOkImageList
import com.rentreadychecklist.constants.Constants.Companion.diningOkImageList
import com.rentreadychecklist.constants.Constants.Companion.frontDoorOkImageList
import com.rentreadychecklist.constants.Constants.Companion.garageOkImageList
import com.rentreadychecklist.constants.Constants.Companion.greatOkImageList
import com.rentreadychecklist.constants.Constants.Companion.imageFormName
import com.rentreadychecklist.constants.Constants.Companion.kitchenOkImageList
import com.rentreadychecklist.constants.Constants.Companion.laundryOkImageList
import com.rentreadychecklist.constants.Constants.Companion.livingOkImageList
import com.rentreadychecklist.constants.Constants.Companion.miscellaneousOkImageList
import com.rentreadychecklist.constants.Constants.Companion.okImagePosition
import com.rentreadychecklist.constants.Constants.Companion.outsideOkImageList
import com.rentreadychecklist.databinding.FragmentOkModelSheetBinding
import com.rentreadychecklist.model.imageupload.ImageUploadCommon
import com.rentreadychecklist.utils.CheckPermission
import com.rentreadychecklist.utils.ImageUpload
import com.rentreadychecklist.utils.ProgressBarDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * This class used for upload, save and delete OK Images.
 */
class OkModelSheetFragment : BottomSheetDialogFragment(), RoomUpdateInterface {

    private lateinit var viewBinding: FragmentOkModelSheetBinding
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var checkPermission: CheckPermission
    private var imageUri: Uri? = null
    private var imageListItem =
        mutableListOf<ImageUploadCommon>()
    private val liveImageList =
        MutableLiveData<MutableList<ImageUploadCommon>>()
    private lateinit var progressBar: ProgressBarDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentOkModelSheetBinding.inflate(inflater, container, false)
        activityResultLauncher(requireContext())
        progressBar = ProgressBarDialog(requireContext())
        checkPermission = CheckPermission(requireContext())
        getImages()
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.imgRecView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, false
        )

        liveImageList.observe(viewLifecycleOwner) {
            progressBar.hideProgressBar()
            viewBinding.imgRecView.adapter = ImageAdapter(
                it, requireContext(),
                this@OkModelSheetFragment
            )
            if (it.isNotEmpty()) {
                viewBinding.imgRecView.visibility = View.VISIBLE
                viewBinding.uploadImageSvg.visibility = View.GONE
            } else {
                viewBinding.imgRecView.visibility = View.GONE
                viewBinding.uploadImageSvg.visibility = View.VISIBLE
            }
        }

        viewBinding.uploadImageBtn.setOnClickListener {
            uploadImageBtnClick()
        }

        viewBinding.saveFix.setOnClickListener {
            saveImages()

        }

    }

    //  save ok data in okList as per the form name passed

    private fun saveImages() {
        when (imageFormName) {
            context?.resources?.getString(R.string.outside_ok) -> {
                outsideOkImageList[okImagePosition].ok = getString(R.string.itemOK)
                outsideOkImageList[okImagePosition].image =
                    getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            context?.resources?.getString(R.string.front_door_ok) -> {
                frontDoorOkImageList[okImagePosition].ok = getString(R.string.itemOK)
                frontDoorOkImageList[okImagePosition].image =
                    getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            context?.resources?.getString(R.string.garage_ok) -> {
                garageOkImageList[okImagePosition].ok = getString(R.string.itemOK)
                garageOkImageList[okImagePosition].image =
                    getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            context?.resources?.getString(R.string.laundry_room_ok) -> {
                laundryOkImageList[okImagePosition].ok = getString(R.string.itemOK)
                laundryOkImageList[okImagePosition].image =
                    getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()

            }
            context?.resources?.getString(R.string.living_room_ok) -> {
                livingOkImageList[okImagePosition].ok = getString(R.string.itemOK)
                livingOkImageList[okImagePosition].image =
                    getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()

            }
            context?.resources?.getString(R.string.great_room_ok) -> {
                greatOkImageList[okImagePosition].ok = getString(R.string.itemOK)
                greatOkImageList[okImagePosition].image =
                    getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            context?.resources?.getString(R.string.dining_room_ok) -> {
                diningOkImageList[okImagePosition].ok = getString(R.string.itemOK)
                diningOkImageList[okImagePosition].image =
                    getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            context?.resources?.getString(R.string.kitchen_ok) -> {
                kitchenOkImageList[okImagePosition].ok = getString(R.string.itemOK)
                kitchenOkImageList[okImagePosition].image =
                    getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            context?.resources?.getString(R.string.miscellaneous_ok) -> {
                miscellaneousOkImageList[okImagePosition].ok = getString(R.string.itemOK)
                miscellaneousOkImageList[okImagePosition].image =
                    getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            context?.resources?.getString(R.string.bedroom_ok) -> {
                bedroomOkImageList[Constants.okImageBedroomPosition][okImagePosition].ok =
                    getString(R.string.itemOK)
                bedroomOkImageList[Constants.okImageBedroomPosition][okImagePosition].image =
                    getImageList(
                        liveImageList
                            .value?.toList()
                    )
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            context?.resources?.getString(R.string.bathroom_ok) -> {
                bathroomOkImageList[Constants.okImageBathroomPosition][okImagePosition].ok =
                    getString(R.string.itemOK)
                bathroomOkImageList[Constants.okImageBathroomPosition][okImagePosition].image =
                    getImageList(
                        liveImageList
                            .value?.toList()
                    )
                activity?.onBackPressedDispatcher?.onBackPressed()

            }
        }
    }


    //set the data as per the okImageList data which is already saved
    private fun getImages() {
        when (imageFormName) {
            context?.resources?.getString(R.string.outside_ok) -> {
                saveImageList(outsideOkImageList[okImagePosition].image)
            }
            context?.resources?.getString(R.string.front_door_ok) -> {
                saveImageList(frontDoorOkImageList[okImagePosition].image)
            }
            context?.resources?.getString(R.string.garage_ok) -> {
                saveImageList(garageOkImageList[okImagePosition].image)
            }
            context?.resources?.getString(R.string.laundry_room_ok) -> {
                saveImageList(laundryOkImageList[okImagePosition].image)
            }
            context?.resources?.getString(R.string.living_room_ok) -> {
                saveImageList(livingOkImageList[okImagePosition].image)
            }
            context?.resources?.getString(R.string.great_room_ok) -> {
                saveImageList(greatOkImageList[okImagePosition].image)
            }
            context?.resources?.getString(R.string.dining_room_ok) -> {
                saveImageList(diningOkImageList[okImagePosition].image)
            }
            context?.resources?.getString(R.string.kitchen_ok) -> {
                saveImageList(kitchenOkImageList[okImagePosition].image)
            }
            context?.resources?.getString(R.string.miscellaneous_ok) -> {
                saveImageList(miscellaneousOkImageList[okImagePosition].image)
            }
            context?.resources?.getString(R.string.bedroom_ok) -> {
                saveImageList(
                    bedroomOkImageList[Constants.okImageBedroomPosition][okImagePosition].image
                )
            }
            context?.resources?.getString(R.string.bathroom_ok) -> {
                saveImageList(
                    bathroomOkImageList[Constants.okImageBathroomPosition][okImagePosition].image
                )
            }
        }
    }

    private fun saveImageList(imageList: List<ImageUploadCommon>) {
        imageListItem.clear()
        imageListItem.addAll(imageList)
        liveImageList.postValue(imageListItem)
    }

    private fun uploadImageBtnClick() {
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

    private fun activityResultLauncher(context: Context) {
        takePhotoLauncher = registerForActivityResult(
            ActivityResultContracts
                .StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (imageUri != null) {
                    try {
                        val imagePath = ImageUpload().getImagePath(context, imageUri!!)
                        val imageData = ImageUploadCommon(imagePath, Constants.okImagesTitle)
                        if (imageData.imagePath.isNotEmpty()) {
                            imageListItem.add(imageData)
                            liveImageList.postValue(imageListItem)
                        }
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

                if (result.data?.clipData != null) {
                    val count = result.data?.clipData?.itemCount!!
                    progressBar.showProgressBar()
                    MainScope().launch(Dispatchers.IO) {

                        for (i in 0 until count) {
                            try {
                                val uri = result.data?.clipData?.getItemAt(i)?.uri
                                if (uri != null) {

                                    val imagePath = ImageUpload().getImagePath(context, uri)
                                    val imageData =
                                        ImageUploadCommon(imagePath, Constants.okImagesTitle)
                                    if (imageData.imagePath.isNotEmpty()) {
                                        imageListItem.add(imageData)
                                    }

                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        liveImageList.postValue(imageListItem)
                    }

                } else if (result.data != null) {

                    val uri = result.data!!.data
                    if (uri != null) {
                        val imagePath = ImageUpload().getImagePath(context, uri)
                        val imageData = ImageUploadCommon(imagePath, Constants.okImagesTitle)
                        if (imageData.imagePath.isNotEmpty()) {
                            imageListItem.add(imageData)
                            liveImageList.postValue(imageListItem)
                        }

                    }

                }

            }

        }
    }

    private fun cameraAndGallerySelectorDialog(context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.image_upload_selector)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)

        val takePhotoBtn = dialog.findViewById<Button>(R.id.takePhotoBtn)
        val galleryBtn = dialog.findViewById<Button>(R.id.galleryPhotoBtn)
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

    private fun getImageList(imgList: List<ImageUploadCommon>?)
            : List<ImageUploadCommon> {
        var imageList = imgList
        if (imageList == null) {
            imageList = arrayListOf()
        }
        return imageList
    }

    override fun checkImageList() {
        super.checkImageList()
        viewBinding.imgRecView.visibility = View.GONE
        viewBinding.uploadImageSvg.visibility = View.VISIBLE
    }


}