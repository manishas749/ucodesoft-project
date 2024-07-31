package com.rentreadychecklist.fragments

import android.app.Activity
import android.app.AlertDialog
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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.rentreadychecklist.R
import com.rentreadychecklist.RoomUpdateInterface
import com.rentreadychecklist.adapters.ImageAdapter
import com.rentreadychecklist.constants.Constants.Companion.FORMID
import com.rentreadychecklist.constants.Constants.Companion.newAndSavedChecklist
import com.rentreadychecklist.databinding.FragmentNewChecklistBinding
import com.rentreadychecklist.db.AppData
import com.rentreadychecklist.formDataSave.FormsDummyData
import com.rentreadychecklist.model.homescreen.HomeScreen
import com.rentreadychecklist.utils.CheckPermission
import com.rentreadychecklist.utils.Helper
import com.rentreadychecklist.utils.ImageUpload
import com.rentreadychecklist.utils.ProgressBarDialog
import com.rentreadychecklist.viewmodel.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Whenever user click on NewChecklist button , a new empty checklist will saved to RoomDatabase
 * and user can edit and saved the Checklist again.
 */
class NewChecklistFragment : Fragment(), RoomUpdateInterface {

    lateinit var viewBinding: FragmentNewChecklistBinding
    private lateinit var viewModel: AppViewModel
    private lateinit var formsDummyData: FormsDummyData
    private lateinit var takePhotoLauncher: ActivityResultLauncher<Intent>
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null
    private var imageListItem =
        mutableListOf<com.rentreadychecklist.model.imageupload.ImageUploadCommon>()
    private val liveImageList =
        MutableLiveData<MutableList<com.rentreadychecklist.model.imageupload.ImageUploadCommon>>()
    private lateinit var progressBar: ProgressBarDialog
    private lateinit var checkPermission: CheckPermission

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        viewBinding = FragmentNewChecklistBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        formsDummyData = FormsDummyData()
        activityResultLauncher(requireContext())
        progressBar = ProgressBarDialog(requireContext())
        checkPermission = CheckPermission(requireContext())
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCurrentDateTime()

        viewBinding.scrollableChild.onFocusChangeListener =
            View.OnFocusChangeListener { v, _ ->
                Helper.hideKeyBoard(v)
            }

        viewBinding.propertyAddress.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    Helper.hideKeyBoard(v)
                }
            }

        viewBinding.next.setOnClickListener {
            Helper.hideKeyBoard(view)
            viewBinding.propertyAddressTextLayout.error = ""
            if (viewBinding.propertyAddress.text.toString() != "") {
                setDataToViewModel()
                try {
                    view.findNavController()
                        .navigate(R.id.action_newChecklistFragment2_to_outsideFormFragment)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                viewBinding.propertyAddressTextLayout.error =
                    resources.getString(R.string.please_enter_address)
            }

        }

        viewBinding.imgRecView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, false
        )

        liveImageList.observe(viewLifecycleOwner) {
            progressBar.hideProgressBar()
            viewBinding.imgRecView.adapter = ImageAdapter(
                it, requireContext(),
                this@NewChecklistFragment
            )
            if (it.isNotEmpty()) {
                viewBinding.imgRecView.visibility = View.VISIBLE
                viewBinding.uploadImageSvg.visibility = View.GONE
            } else {
                viewBinding.imgRecView.visibility = View.GONE
                viewBinding.uploadImageSvg.visibility = View.VISIBLE
            }
        }

        setDataOnAdapter()


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

    //to set data in adapter if already saved in viewModel/Database home-screen and
    // images which is already uploaded

    fun setDataOnAdapter() {
        try {
            viewModel.readFormDataID.observe(viewLifecycleOwner) {

                if (newAndSavedChecklist == context?.resources?.getString(R.string.saved)) {
                    if (it.isEmpty()) {
                        showListDeleteDialog()
                    } else {
                        formsDummyData.clearConstantData()
                        addDataToSaveChecklist(it)
                        FormsDummyData.homeScreenData = it[0].homeScreen.toMutableList()
                        if (FormsDummyData.homeScreenData[0].time.isNotEmpty()) {
                            viewBinding.propertyAddress.setText(FormsDummyData.homeScreenData[0].propertyAddress)
                            viewBinding.timeTextInput.text = FormsDummyData.homeScreenData[0].time
                            viewBinding.dateTextInput.text = FormsDummyData.homeScreenData[0].date
                            viewBinding.addressCheckbox.isChecked = FormsDummyData.homeScreenData[0]
                                .addressCondition
                            viewBinding.frontDoorLockCheckbox.isChecked =
                                FormsDummyData.homeScreenData[0]
                                    .frontDoorCondition
                            viewBinding.lockSetFromGarageCheckbox.isChecked =
                                FormsDummyData.homeScreenData[0]
                                    .lockSetCondition
                            imageListItem.clear()
                            imageListItem.addAll(FormsDummyData.homeScreenData[0].image.toMutableList())
                            if (imageListItem.isNotEmpty()) {
                                liveImageList.postValue(imageListItem)
                            }
                        }

                    }
                }

            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showListDeleteDialog() {
        val listDialog = Dialog(requireContext())
        listDialog.setContentView(R.layout.listexist_dialog)
        val yesButton = listDialog.findViewById<Button>(R.id.yes)
        val noButton = listDialog.findViewById<Button>(R.id.no)
        listDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        listDialog.show()
        yesButton.setOnClickListener {
            listDialog.dismiss()
            Navigation.findNavController(viewBinding.root).popBackStack()
        }
        noButton.setOnClickListener {
            listDialog.dismiss()
        }
    }


    private fun addDataToSaveChecklist(list: List<AppData>) {
        val checklist = list[0]
        FormsDummyData.outsideData = checklist.outside.toMutableList()
        FormsDummyData.frontDoorsData = checklist.frontDoors.toMutableList()
        FormsDummyData.garageData = checklist.garage.toMutableList()
        FormsDummyData.laundryRoomData = checklist.laundryRoom.toMutableList()
        FormsDummyData.livingRoomData = checklist.livingRoom.toMutableList()
        FormsDummyData.greatRoomData = checklist.greatRoom.toMutableList()
        FormsDummyData.diningRoomData = checklist.diningRoom.toMutableList()
        FormsDummyData.kitchenData = checklist.kitchen.toMutableList()
        FormsDummyData.miscellaneousData = checklist.miscellaneous.toMutableList()
        FormsDummyData.numberOfBedroom = checklist.bedroom.toMutableList()
        FormsDummyData.numberOfBathroom = checklist.bathroom.toMutableList()

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
                                        com.rentreadychecklist.model.imageupload.ImageUploadCommon(
                                            imagePath,
                                            context.resources.getString(R.string.addressLockSet)
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
                        saveImage(uri, context)
                    }

                }
            }
        }
    }

    private fun saveImage(imageUri: Uri, context: Context) {
        val imagePath = ImageUpload().getImagePath(context, imageUri)
        val imageData = com.rentreadychecklist.model.imageupload.ImageUploadCommon(
            imagePath,
            context.resources.getString(R.string.addressLockSet)
        )
        if (imageData.imagePath.isNotEmpty()) {
            imageListItem.add(imageData)
            liveImageList.postValue(imageListItem)
        }
    }

    //set current date and time

    private fun getCurrentDateTime() {
        val currentDateYear = SimpleDateFormat(
            context?.resources?.getString(R.string.ddMMMyy),
            Locale.getDefault()
        )
        val currentDate = currentDateYear.format(Date())
        val currentTime = SimpleDateFormat(
            context?.resources?.getString(R.string.hourMinuteFormat),
            Locale.getDefault()
        ).format(
            Date()
        )
        viewBinding.timeTextInput.text = currentTime
        viewBinding.dateTextInput.text = currentDate


    }

    //set and save data in viewModel/Database HomeScreen

    private fun setDataToViewModel() {

        val currentDate = viewBinding.dateTextInput.text
        val currentTime = viewBinding.timeTextInput.text
        val propertyAddress = viewBinding.propertyAddress.text
        val homeScreenData: MutableList<HomeScreen> = mutableListOf()
        var imgList = liveImageList.value?.toList()

        if (imgList == null) {
            imgList = arrayListOf()
        }

        var additionalNotes = ""
        if(FormsDummyData.homeScreenData.isNotEmpty()){
            additionalNotes = FormsDummyData.homeScreenData[0].additionalNotes
        }
        homeScreenData.add(
            HomeScreen(
                currentDate.toString(),
                currentTime.toString(),
                propertyAddress.toString().trim(),
                viewBinding.addressCheckbox.isChecked,
                viewBinding.frontDoorLockCheckbox.isChecked,
                viewBinding.lockSetFromGarageCheckbox.isChecked,
                imgList, additionalNotes
            )
        )

        if(FormsDummyData.homeScreenData.isNotEmpty()){
        FormsDummyData.homeScreenData[0] = homeScreenData[0]
        }
        viewModel.updateHomeScreen(homeScreenData, FORMID)

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

    override fun checkImageList() {
        super.checkImageList()
        viewBinding.imgRecView.visibility = View.GONE
        viewBinding.uploadImageSvg.visibility = View.VISIBLE

    }

}