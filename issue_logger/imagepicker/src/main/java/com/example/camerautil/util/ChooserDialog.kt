package com.example.camerautil.util

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import com.example.camerautil.R
import com.example.camerautil.camera.CameraProvider
import com.example.camerautil.gallery.GalleryProvider
import com.example.camerautil.launcher.CameraLauncher
import com.example.camerautil.launcher.GalleryLauncher
import com.example.camerautil.listener.ChooserClickListener
import com.example.camerautil.provider.RequestCodeProvider

/***
 *
 * ChooserDialog
 *
 ***/
class ChooserDialog(
    private val activity: Activity,
    private val cameraProvider: CameraProvider,
    private val galleryProvider: GalleryProvider,
    private val cameraLauncher: CameraLauncher,
    private val galleryLauncher: GalleryLauncher
) {

    private var intent: Intent? = null

    //chooser dialog with listener or user custom layout
    fun chooserDialogWithListener(
        activity: Activity, listener: ChooserClickListener, customLayout: Int = R.layout.camera_gallery_chooser_dialog
    ) {
        val dialog = Dialog(activity)

        //set dialog properties
        dialog.setContentView(customLayout)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        var intent: Intent

        /**
         *
         * if no layout is provide by user
         *  * if block will execute
         *  * where listener onclick method is executed
         *
         * else
         *  * else block will execute
         *  * where listener customDialogLayout method is executed
         *
         **/

        if (customLayout == R.layout.camera_gallery_chooser_dialog) {

            //finding views from dialog
            val cameraBtn = dialog.findViewById<TextView>(R.id.cameraBtn)
            val galleryBtn = dialog.findViewById<TextView>(R.id.galleryBtn)
            val cancelBtn = dialog.findViewById<TextView>(R.id.cancelBtn)

            cameraBtn.setOnClickListener {
                intent = getCameraIntent()      //set camera intent to intent
                listener.onClick(RequestCodeProvider.CAMERA_REQ_CODE, intent)
                dialog.dismiss()
            }

            galleryBtn.setOnClickListener {
                intent = getGalleryIntent()     //set gallery intent to intent
                listener.onClick(RequestCodeProvider.GALLERY_REQ_CODE, intent)
                dialog.dismiss()
            }

            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()

        } else {
            //set dialog to listener
            listener.customDialogLayout(dialog)
        }
    }

    //default chooser dialog
    fun chooserDialog(activity: Activity, createIntent: Boolean) {
        val dialog = Dialog(activity)

        //set dialog properties
        dialog.setContentView(R.layout.camera_gallery_chooser_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        //finding views from dialog
        val cameraBtn = dialog.findViewById<TextView>(R.id.cameraBtn)
        val galleryBtn = dialog.findViewById<TextView>(R.id.galleryBtn)
        val cancelBtn = dialog.findViewById<TextView>(R.id.cancelBtn)

        cameraBtn.setOnClickListener {
            intent = getCameraIntent()      //set camera intent to intent
            if(!createIntent) {
                cameraLauncher.cameraLauncher.launch(intent)        //launch default camera launcher with camera intent
            }
            dialog.dismiss()
        }

        galleryBtn.setOnClickListener {
            intent = getGalleryIntent()     //set gallery intent to intent
            if(!createIntent) {
                galleryLauncher.galleryLauncher.launch(intent)      //launch default gallery launcher with gallery intent
            }
            dialog.dismiss()
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    //return camera intent
    private fun getCameraIntent(): Intent = cameraProvider.cameraIntent(activity)

    //get gallery intent
    private fun getGalleryIntent(): Intent = getMultipleImageGalleryIntent()

    //get multiple selection of image from gallery intent
    private fun getMultipleImageGalleryIntent(): Intent =
        galleryProvider.multipleImageGalleryIntent()
}