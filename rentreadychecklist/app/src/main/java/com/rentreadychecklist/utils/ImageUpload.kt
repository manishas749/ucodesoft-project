package com.rentreadychecklist.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * This class use for save image to specific folder and image rotation.
 */
class ImageUpload {

    //Save image to folder with current data and time and this function will return image path.
    private fun saveImageToFolder(bm: Bitmap): String {
        val image = File(
            getOutputDirectory(), SimpleDateFormat(
                "yy-MM-dd-HH-mm-ss-SS",
                Locale.getDefault()
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        if (image.exists()) {
            image.delete()
        }

        try {
            val out = FileOutputStream(image)
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return image.path
    }

    //This function return Folder Directory.
    private fun getOutputDirectory(): File {
        val path = Environment.getExternalStorageDirectory().toString() + "/DCIM/.RentReady"
        val direct = File(path)

        if (!direct.exists()) {
            direct.mkdir()
        }

        return direct
    }

    fun getImagePath(context: Context, uri: Uri): String {

        var imagePath = ""
        try {
            val ims: InputStream = context.contentResolver.openInputStream(uri)!!
            val rotation = getRotation(context, uri)
            if (rotation != 0) {
                val matrix = Matrix()
                when (rotation) {
                    180 -> {
                        matrix.postRotate(180f)
                    }
                    270 -> {
                        matrix.postRotate(270f)
                    }
                    else -> {
                        matrix.postRotate(90f)
                    }
                }

                val bm = BitmapFactory.decodeStream(ims)
                val rotatedBitmap = Bitmap.createBitmap(
                    bm,
                    0,
                    0,
                    bm.width,
                    bm.height,
                    matrix,
                    true
                )
                if (rotatedBitmap != null) {
                    imagePath = ImageUpload().saveImageToFolder(rotatedBitmap)
                }
            } else {
                val bm = BitmapFactory.decodeStream(ims)
                if (bm != null) {
                    imagePath = ImageUpload().saveImageToFolder(bm)
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
        return imagePath
    }


    //This function return the image angle.
    private fun getRotation(context: Context, uri: Uri): Int {
        var rotation = 0
        try {
            val ims: InputStream = context.contentResolver.openInputStream(uri)!!
            val exif = ExifInterface(ims)
            rotation =
                exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL
                )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return exifToDegrees(rotation)
    }

    //This function will return degree of image
    private fun exifToDegrees(exifOrientation: Int): Int {
        return when (exifOrientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> {
                90
            }
            ExifInterface.ORIENTATION_ROTATE_180 -> {
                180
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> {
                270
            }
            else -> 0
        }
    }

}