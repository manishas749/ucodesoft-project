package com.example.camerautil.file

import android.content.Context
import android.graphics.Bitmap
import android.provider.MediaStore
import com.example.camerautil.provider.ImageFormatProvider.Companion.JPG
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/***
 *
 * FileProvider class
 *      * provider files related functions for user
 *
 ***/

class FileProvider {

    //function for creating directory
    fun getDir(path: String): File {
        val direct = File(path)
        if (!direct.exists()) {
            direct.mkdir()
        }
        return direct
    }

    //getting all images form directory
    fun getImagesFromFile(dir: File): ArrayList<String> {
        val list = ArrayList<String>()
        if (dir.listFiles() != null) {
            for (file in dir.listFiles()!!) {
                list.add(file.absolutePath)
            }
        }
        return list
    }

    //saving image in directory
    fun saveImageToFolder(bm: Bitmap, dir: File, name: String) {

        //creating image file
        val image = File(
            dir,
            SimpleDateFormat(name, Locale.getDefault()).format(System.currentTimeMillis()) + JPG
        )

        if (image.exists()) {
            image.delete()
        }

        //saving image file
        try {
            val out = FileOutputStream(image)
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //fetching all images from gallery
    fun fetchImagesFromGallery(context: Context): ArrayList<String> {
        val list = ArrayList<String>()

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val pro = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val orderBy = MediaStore.Images.Media.DATE_TAKEN

        val cursor = context.contentResolver.query(uri, pro, null, null, "$orderBy DESC")
        val index = cursor?.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val imagePath = cursor.getString(index!!)
                list.add(imagePath)
            }
        }
        cursor?.close()
        return list
    }
}