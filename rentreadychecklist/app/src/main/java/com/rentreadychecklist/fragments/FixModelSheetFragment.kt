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
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rentreadychecklist.R
import com.rentreadychecklist.RoomUpdateInterface
import com.rentreadychecklist.adapters.ImageAdapter
import com.rentreadychecklist.constants.Constants.Companion.bathroomFixList
import com.rentreadychecklist.constants.Constants.Companion.bathroomFixPosition
import com.rentreadychecklist.constants.Constants.Companion.bedroomFixList
import com.rentreadychecklist.constants.Constants.Companion.bedroomFixPosition
import com.rentreadychecklist.constants.Constants.Companion.diningRoomFixList
import com.rentreadychecklist.constants.Constants.Companion.fixList
import com.rentreadychecklist.constants.Constants.Companion.fixPosition
import com.rentreadychecklist.constants.Constants.Companion.formName
import com.rentreadychecklist.constants.Constants.Companion.frontDoorFixList
import com.rentreadychecklist.constants.Constants.Companion.garageFixList
import com.rentreadychecklist.constants.Constants.Companion.greatRoomFixList
import com.rentreadychecklist.constants.Constants.Companion.imageTitle
import com.rentreadychecklist.constants.Constants.Companion.kitchenFixList
import com.rentreadychecklist.constants.Constants.Companion.laundryFixList
import com.rentreadychecklist.constants.Constants.Companion.livingRoomFixList
import com.rentreadychecklist.constants.Constants.Companion.miscellaneousFixList
import com.rentreadychecklist.databinding.FragmentFixModelSheetBinding
import com.rentreadychecklist.model.bathroom.Bathroom
import com.rentreadychecklist.model.bedrooms.Bedroom
import com.rentreadychecklist.model.diningRoom.DiningRoom
import com.rentreadychecklist.model.frontdoors.FrontDoors
import com.rentreadychecklist.model.garage.Garage
import com.rentreadychecklist.model.greatroom.GreatRoom
import com.rentreadychecklist.model.imageupload.ImageUploadCommon
import com.rentreadychecklist.model.kitchen.Kitchen
import com.rentreadychecklist.model.laundry.LaundryRoom
import com.rentreadychecklist.model.livingRoom.LivingRoom
import com.rentreadychecklist.model.miscellaneous.Miscellaneous
import com.rentreadychecklist.model.outside.Outside
import com.rentreadychecklist.utils.CheckPermission
import com.rentreadychecklist.utils.Helper
import com.rentreadychecklist.utils.ImageUpload
import com.rentreadychecklist.utils.ProgressBarDialog
import com.rentreadychecklist.viewmodel.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * This class used for upload, save and delete Fix Images. And user can
 * edit and save Notes, Time, Product name.
 */
class FixModelSheetFragment : BottomSheetDialogFragment(), RoomUpdateInterface {
    lateinit var viewBinding: FragmentFixModelSheetBinding
    private lateinit var outsideItemList: MutableList<Outside>
    private lateinit var frontDoorItemList: MutableList<FrontDoors>
    private lateinit var garageItemList: MutableList<Garage>
    private lateinit var laundryItemList: MutableList<LaundryRoom>
    private lateinit var livingRoomItemList: MutableList<LivingRoom>
    private lateinit var greatRoomItemList: MutableList<GreatRoom>
    private lateinit var diningRoomItemList: MutableList<DiningRoom>
    private lateinit var kitchenRoomItemList: MutableList<Kitchen>
    private lateinit var miscellaneousItemList: MutableList<Miscellaneous>
    private lateinit var bedroomItemList: MutableList<Bedroom>
    private lateinit var bathroomItemList: MutableList<Bathroom>

    lateinit var viewModel: AppViewModel
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
        viewBinding = FragmentFixModelSheetBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        outsideItemList = mutableListOf()
        frontDoorItemList = mutableListOf()
        garageItemList = mutableListOf()
        laundryItemList = mutableListOf()
        livingRoomItemList = mutableListOf()
        greatRoomItemList = mutableListOf()
        diningRoomItemList = mutableListOf()
        kitchenRoomItemList = mutableListOf()
        miscellaneousItemList = mutableListOf()
        bedroomItemList = mutableListOf()
        bathroomItemList = mutableListOf()
        checkPermission = CheckPermission(requireContext())
        getDataObserver()
        activityResultLauncher(requireContext())
        progressBar = ProgressBarDialog(requireContext())
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        keyboardHideOnFocusChangeListener()

        viewBinding.saveFix.setOnClickListener {
            saveFixDetails()
        }

        viewBinding.imgRecView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, false
        )

        liveImageList.observe(viewLifecycleOwner) {
            progressBar.hideProgressBar()
            viewBinding.imgRecView.adapter = ImageAdapter(
                it, requireContext(),
                this@FixModelSheetFragment
            )
            if (it.isNotEmpty()) {
                viewBinding.imgRecView.visibility = View.VISIBLE
                viewBinding.uploadImageSvg.visibility = View.GONE
            } else {
                viewBinding.imgRecView.visibility = View.GONE
                viewBinding.uploadImageSvg.visibility = View.VISIBLE
            }
        }

    }

    override fun onResume() {
        super.onResume()

        viewBinding.uploadImageBtn.setOnClickListener {
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
    }

    private fun keyboardHideOnFocusChangeListener() {
        viewBinding.needFixLinearLayout.onFocusChangeListener = OnFocusChangeListener { v, _ ->
            Helper.hideKeyBoard(v)
        }
    }

    //  save fix data in fixList as per the form name passed
    private fun saveFixDetails() {
        when (formName) {
            context?.resources?.getString(R.string.formOutside) -> {
                fixList[fixPosition].time = viewBinding.timeInHourMinute.text.toString()
                fixList[fixPosition].product = viewBinding.product.text.toString().trim()
                fixList[fixPosition].notes = viewBinding.notes.text.toString().trim()
                fixList[fixPosition].fix = context?.getString(R.string.itemFIX).toString()
                fixList[fixPosition].image = getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()
            }

            context?.resources?.getString(R.string.formFrontDoor) -> {
                frontDoorFixList[fixPosition].time = viewBinding.timeInHourMinute.text.toString()
                frontDoorFixList[fixPosition].product = viewBinding.product.text.toString().trim()
                frontDoorFixList[fixPosition].notes = viewBinding.notes.text.toString().trim()
                frontDoorFixList[fixPosition].fix = context?.getString(R.string.itemFIX).toString()
                frontDoorFixList[fixPosition].image = getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()
            }

            context?.resources?.getString(R.string.formGarage) -> {
                garageFixList[fixPosition].time = viewBinding.timeInHourMinute.text.toString()
                garageFixList[fixPosition].product = viewBinding.product.text.toString().trim()
                garageFixList[fixPosition].notes = viewBinding.notes.text.toString().trim()
                garageFixList[fixPosition].fix = context?.getString(R.string.itemFIX).toString()
                garageFixList[fixPosition].image = getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()
            }

            context?.resources?.getString(R.string.formLaundry) -> {
                laundryFixList[fixPosition].time = viewBinding.timeInHourMinute.text.toString()
                laundryFixList[fixPosition].product = viewBinding.product.text.toString().trim()
                laundryFixList[fixPosition].notes = viewBinding.notes.text.toString().trim()
                laundryFixList[fixPosition].fix = context?.getString(R.string.itemFIX).toString()
                laundryFixList[fixPosition].image = getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            context?.resources?.getString(R.string.formLivingRoom) -> {
                livingRoomFixList[fixPosition].time = viewBinding.timeInHourMinute.text.toString()
                livingRoomFixList[fixPosition].product = viewBinding.product.text.toString().trim()
                livingRoomFixList[fixPosition].notes = viewBinding.notes.text.toString().trim()
                livingRoomFixList[fixPosition].fix = context?.getString(R.string.itemFIX).toString()
                livingRoomFixList[fixPosition].image = getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()
            }

            context?.resources?.getString(R.string.formGreatRoom) -> {
                greatRoomFixList[fixPosition].time = viewBinding.timeInHourMinute.text.toString()
                greatRoomFixList[fixPosition].product = viewBinding.product.text.toString().trim()
                greatRoomFixList[fixPosition].notes = viewBinding.notes.text.toString().trim()
                greatRoomFixList[fixPosition].fix = context?.getString(R.string.itemFIX).toString()
                greatRoomFixList[fixPosition].image = getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()
            }

            context?.resources?.getString(R.string.formDiningRoom) -> {
                diningRoomFixList[fixPosition].time = viewBinding.timeInHourMinute.text.toString()
                diningRoomFixList[fixPosition].product = viewBinding.product.text.toString().trim()
                diningRoomFixList[fixPosition].notes = viewBinding.notes.text.toString().trim()
                diningRoomFixList[fixPosition].fix = context?.getString(R.string.itemFIX).toString()
                diningRoomFixList[fixPosition].image = getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()
            }

            context?.resources?.getString(R.string.kitchen) -> {
                kitchenFixList[fixPosition].time = viewBinding.timeInHourMinute.text.toString()
                kitchenFixList[fixPosition].product = viewBinding.product.text.toString().trim()
                kitchenFixList[fixPosition].notes = viewBinding.notes.text.toString().trim()
                kitchenFixList[fixPosition].fix = context?.getString(R.string.itemFIX).toString()
                kitchenFixList[fixPosition].image = getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()
            }

            context?.resources?.getString(R.string.miscellaneous) -> {
                miscellaneousFixList[fixPosition].time = viewBinding.timeInHourMinute.text
                    .toString()
                miscellaneousFixList[fixPosition].product = viewBinding.product.text.toString()
                    .trim()
                miscellaneousFixList[fixPosition].notes = viewBinding.notes.text.toString()
                    .trim()
                miscellaneousFixList[fixPosition].fix =
                    context?.getString(R.string.itemFIX).toString()
                miscellaneousFixList[fixPosition].image =
                    getImageList(liveImageList.value?.toList())
                activity?.onBackPressedDispatcher?.onBackPressed()
            }

            context?.resources?.getString(R.string.bedroom) -> {
                bedroomFixList[bedroomFixPosition][fixPosition].time = viewBinding.timeInHourMinute
                    .text.toString()
                bedroomFixList[bedroomFixPosition][fixPosition].product = viewBinding.product.text
                    .toString().trim()
                bedroomFixList[bedroomFixPosition][fixPosition].notes = viewBinding.notes
                    .text.toString().trim()
                bedroomFixList[bedroomFixPosition][fixPosition].fix =
                    context?.getString(R.string.itemFIX).toString()
                bedroomFixList[bedroomFixPosition][fixPosition].image = getImageList(
                    liveImageList
                        .value?.toList()
                )
                activity?.onBackPressedDispatcher?.onBackPressed()
            }

            context?.resources?.getString(R.string.formBathroom) -> {
                bathroomFixList[bathroomFixPosition][fixPosition].time = viewBinding
                    .timeInHourMinute.text.toString()
                bathroomFixList[bathroomFixPosition][fixPosition].product = viewBinding
                    .product.text.toString().trim()
                bathroomFixList[bathroomFixPosition][fixPosition].notes = viewBinding.notes.text
                    .toString().trim()
                bathroomFixList[bathroomFixPosition][fixPosition].fix =
                    context?.getString(R.string.itemFIX).toString()
                bathroomFixList[bathroomFixPosition][fixPosition].image = getImageList(
                    liveImageList
                        .value?.toList()
                )
                activity?.onBackPressedDispatcher?.onBackPressed()
            }

        }
    }


    //set the data as per the fixList data which is already saved
    private fun getDataObserver() {
        when (formName) {

            context?.resources?.getString(R.string.formOutside) -> {
                setDataOnViews(
                    fixList[fixPosition].time,
                    fixList[fixPosition].product,
                    fixList[fixPosition].notes,
                    fixList[fixPosition].image
                )
            }

            context?.resources?.getString(R.string.formFrontDoor) -> {
                setDataOnViews(
                    frontDoorFixList[fixPosition].time,
                    frontDoorFixList[fixPosition].product,
                    frontDoorFixList[fixPosition].notes,
                    frontDoorFixList[fixPosition].image
                )
            }

            context?.resources?.getString(R.string.formGarage) -> {
                setDataOnViews(
                    garageFixList[fixPosition].time,
                    garageFixList[fixPosition].product,
                    garageFixList[fixPosition].notes,
                    garageFixList[fixPosition].image
                )
            }

            context?.resources?.getString(R.string.formLaundry) -> {
                setDataOnViews(
                    laundryFixList[fixPosition].time,
                    laundryFixList[fixPosition].product,
                    laundryFixList[fixPosition].notes,
                    laundryFixList[fixPosition].image
                )
            }

            context?.resources?.getString(R.string.formLivingRoom) -> {
                setDataOnViews(
                    livingRoomFixList[fixPosition].time,
                    livingRoomFixList[fixPosition].product,
                    livingRoomFixList[fixPosition].notes,
                    livingRoomFixList[fixPosition].image
                )
            }

            context?.resources?.getString(R.string.formGreatRoom) -> {
                setDataOnViews(
                    greatRoomFixList[fixPosition].time,
                    greatRoomFixList[fixPosition].product,
                    greatRoomFixList[fixPosition].notes,
                    greatRoomFixList[fixPosition].image
                )
            }

            context?.resources?.getString(R.string.formDiningRoom) -> {
                setDataOnViews(
                    diningRoomFixList[fixPosition].time,
                    diningRoomFixList[fixPosition].product,
                    diningRoomFixList[fixPosition].notes,
                    diningRoomFixList[fixPosition].image
                )
            }

            context?.resources?.getString(R.string.kitchen) -> {
                setDataOnViews(
                    kitchenFixList[fixPosition].time,
                    kitchenFixList[fixPosition].product,
                    kitchenFixList[fixPosition].notes,
                    kitchenFixList[fixPosition].image
                )
            }

            context?.resources?.getString(R.string.miscellaneous) -> {
                setDataOnViews(
                    miscellaneousFixList[fixPosition].time,
                    miscellaneousFixList[fixPosition].product,
                    miscellaneousFixList[fixPosition].notes,
                    miscellaneousFixList[fixPosition].image
                )
            }

            context?.resources?.getString(R.string.bedroom) -> {
                setDataOnViews(
                    bedroomFixList[bedroomFixPosition][fixPosition].time,
                    bedroomFixList[bedroomFixPosition][fixPosition].product,
                    bedroomFixList[bedroomFixPosition][fixPosition].notes,
                    bedroomFixList[bedroomFixPosition][fixPosition].image
                )
            }

            context?.resources?.getString(R.string.formBathroom) -> {
                setDataOnViews(
                    bathroomFixList[bathroomFixPosition]
                            [fixPosition].time, bathroomFixList[bathroomFixPosition][fixPosition]
                        .product, bathroomFixList[bathroomFixPosition][fixPosition]
                        .notes, bathroomFixList[bathroomFixPosition][fixPosition].image
                )
            }
        }
    }



    private fun setDataOnViews(
        time: String,
        product: String,
        notes: String,
        imageList: List<ImageUploadCommon>
    ) {
        viewBinding.timeInHourMinute.setText(time)
        viewBinding.product.setText(product)
        viewBinding.notes.setText(notes)
        imageListItem.clear()
        imageListItem.addAll(imageList)
        liveImageList.postValue(imageListItem)
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
                        val imageData = ImageUploadCommon(
                            imagePath,
                            imageTitle
                        )
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
                                        ImageUploadCommon(
                                            imagePath,
                                            imageTitle
                                        )
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
                        val imageData = ImageUploadCommon(
                            imagePath,
                            imageTitle
                        )
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

    private fun getImageList(imgList: List<ImageUploadCommon>?): List<ImageUploadCommon> {
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