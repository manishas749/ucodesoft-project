package com.example.camerautil.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import java.io.InputStream

/***
 *
 * BitmapUtil
 *
 ***/
class BitmapUtil {

    companion object{
        //method to get bitmap from image uri
        fun getBitmapFromUri(activity: AppCompatActivity, imageUri: Uri?): Bitmap? {
            val iS: InputStream? = activity.contentResolver.openInputStream(imageUri!!)
            val bitmap = BitmapFactory.decodeStream(iS)
            iS?.close()
            return bitmap
        }

        fun rotateBitmap(bitmap: Bitmap, i: Int): Bitmap? {
            val matrix = Matrix()
            matrix.postScale(1F, 1F)
            if (i == 0) {
                matrix.postRotate(-90F)
            } else {
                matrix.postRotate(90F)
            }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }

        fun createFlippedBitmap(bitmap: Bitmap, xFlip: Boolean, yFlip: Boolean): Bitmap? {
            val matrix = Matrix()
            matrix.postScale(
                if (xFlip) -1F else 1F, if (yFlip) -1F else 1F, bitmap.width / 2f, bitmap.height / 2f
            )
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
    }
}