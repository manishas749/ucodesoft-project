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
import com.example.camerautil.provider.ImageProvider
import com.example.camerautil.util.BitmapUtil.Companion.getBitmapFromUri
import com.example.camerautil.util.Helper.Companion.applyCompressOrResize

class GalleryLauncher(activity: AppCompatActivity? = null, fragment: Fragment? = null) {

    companion object {
        var galImageUri: Uri? = null       //for single image uri
        var arrayOfUri: ArrayList<Uri?> = ArrayList()        //for multiple images uri
        var galBitmap: Bitmap? = null     //for single image bitmap
        var arrayOfBitmap: ArrayList<Bitmap?> = ArrayList()      //for multiple image bitmap
    }

    val galleryLauncher =       //default launcher for gallery intent
        activity?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {

                arrayOfUri.clear()          //clear list for updation
                arrayOfBitmap.clear()       //clear list for updation

                if (result.data?.clipData != null) {

                    val list = result.data?.clipData!!

                    for (image in 0 until list.itemCount) {
                        val imageUri = list.getItemAt(image)?.uri!!         // get image uri

                        arrayOfUri.add(imageUri)        //image uri to list

                        //get bitmap from image uri
                        val bm = getBitmapFromUri(activity, imageUri)

                        arrayOfBitmap.add(bm)       //add bitmap to the list
                    }

                } else {
                    galImageUri =
                        result.data?.data!!      //get image uri and set imageUri to uri

                    arrayOfUri.add(galImageUri!!)     //add imageUri to the list

                    //get bitmap from image uri and set it to bitmap
                    galBitmap = getBitmapFromUri(activity, galImageUri)

                    arrayOfBitmap.add(galBitmap!!)       //add bitmap to the list

                    galBitmap = applyEditing(activity)
                }
            } else {
                arrayOfUri.clear()          //clear list for updation
                arrayOfBitmap.clear()       //clear list for updation
                Toast.makeText(activity, "No photo selected", Toast.LENGTH_SHORT).show()
            }

        }
            ?: if (fragment != null) {
                fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

                    if (result.resultCode == Activity.RESULT_OK) {

                        arrayOfUri.clear()          //clear list for updation
                        arrayOfBitmap.clear()       //clear list for updation

                        if (result.data?.clipData != null) {

                            val list = result.data?.clipData!!

                            for (image in 0 until list.itemCount) {
                                val imageUri = list.getItemAt(image)?.uri!!         // get image uri

                                arrayOfUri.add(imageUri)        //image uri to list

                                //get bitmap from image uri
                                val bm = getBitmapFromUri(fragment.requireActivity() as AppCompatActivity, imageUri)

                                arrayOfBitmap.add(bm)       //add bitmap to the list
                            }

                        } else {
                            galImageUri =
                                result.data?.data!!      //get image uri and set imageUri to uri

                            arrayOfUri.add(galImageUri!!)     //add imageUri to the list

                            //get bitmap from image uri and set it to bitmap
                            galBitmap = getBitmapFromUri(fragment.requireActivity() as AppCompatActivity, galImageUri)

                            arrayOfBitmap.add(galBitmap!!)       //add bitmap to the list

                            galBitmap = applyEditing(fragment.requireActivity())
                        }
                    } else {
                        arrayOfUri.clear()          //clear list for updation
                        arrayOfBitmap.clear()       //clear list for updation
                        Toast.makeText(fragment.requireContext(), "No photo selected", Toast.LENGTH_SHORT).show()
                    }

                }
            } else {
                throw NullPointerException()
            }

    private fun applyEditing(activity: Activity): Bitmap? {
        var tempBitmap: Bitmap? = null
        if (isCropEnabled) {        //crop image

            val intent = Intent(activity, ImagePickerActivity::class.java)
            intent.putExtra("imageProvider", ImageProvider.GALLERY.name)
            activity.startActivity(intent)

        } else {
            tempBitmap = applyCompressOrResize(galBitmap)
        }
        return tempBitmap
    }

    fun getUri(): Uri? = galImageUri     // return image uri

    fun getUris(): ArrayList<Uri?> =     //return list of image uri
        if (arrayOfUri.size == 1) arrayListOf(galImageUri)
        else arrayOfUri

    fun getBitmap(): Bitmap? = galBitmap        //return image bitmap

    fun getBitmaps(): ArrayList<Bitmap?> =       //return list of image bitmap
        if (arrayOfBitmap.size == 1) arrayListOf(galBitmap)
        else arrayOfBitmap

}