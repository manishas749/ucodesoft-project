package com.example.camerautil.launcher

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.camerautil.ImagePicker.Companion.isCropEnabled
import com.example.camerautil.ImagePickerActivity
import com.example.camerautil.camera.CameraProvider.Companion.camImageUri
import com.example.camerautil.provider.ImageProvider
import com.example.camerautil.util.BitmapUtil.Companion.getBitmapFromUri
import com.example.camerautil.util.Helper.Companion.applyCompressOrResize


class CameraLauncher(private val activity: AppCompatActivity? = null, fragment: Fragment? = null) {

    companion object {
        var camBitmap: Bitmap? = null      //for image bitmap
    }

    val cameraLauncher =        //default launcher for camera intent
        activity?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                camBitmap =
                    getBitmapFromUri(
                        activity,
                        camImageUri
                    )      //get bitmap from uri and set it to bitmap

                if (isCropEnabled) {
                    val intent = Intent(activity, ImagePickerActivity::class.java)
                    intent.putExtra("imageProvider", ImageProvider.CAMERA.name)
                    activity.startActivity(intent)
                } else {
                    camBitmap = applyCompressOrResize(camBitmap)
                }
            } else {
                Toast.makeText(activity, "No image clicked", Toast.LENGTH_SHORT).show()
            }
        }
            ?: if (fragment != null) {
                fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {

                        camBitmap =
                            getBitmapFromUri(
                                fragment.requireActivity() as AppCompatActivity,
                                camImageUri
                            )      //get bitmap from uri and set it to bitmap

                        if (isCropEnabled) {
                            val intent = Intent(activity, ImagePickerActivity::class.java)
                            intent.putExtra("imageProvider", ImageProvider.CAMERA.name)
                            fragment.startActivity(intent)
                        } else {
                            camBitmap = applyCompressOrResize(camBitmap)
                        }
                    } else {
                        Toast.makeText(activity, "No image clicked", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                throw NullPointerException()
            }

    fun getBitmap(): Bitmap? = camBitmap       //return bitmap

    fun getUri(): Uri? = camImageUri
}