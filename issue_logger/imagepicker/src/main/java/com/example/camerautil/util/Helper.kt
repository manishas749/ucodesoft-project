package com.example.camerautil.util

import android.graphics.Bitmap
import com.example.camerautil.ImagePicker.Companion.DEFAULT_COMPRESSION_QUALITY
import com.example.camerautil.ImagePicker.Companion.compressQuality
import com.example.camerautil.ImagePicker.Companion.isCompEnabled
import com.example.camerautil.ImagePicker.Companion.isReSizeEnabled
import com.example.camerautil.compression.CompressionProvider.Companion.compressBitmap
import com.example.camerautil.compression.CompressionProvider.Companion.resizeImage

class Helper {

    companion object {
        fun applyCompressOrResize(orgBitmap: Bitmap?): Bitmap? {
            var tempBm :Bitmap? = orgBitmap

            if (isCompEnabled) {
                if (compressQuality != null) {        //compress image according to user defined
                    tempBm = compressBitmap(tempBm, compressQuality!!)
                    compressQuality = null
                } else {        //compress image according to default
                    tempBm = compressBitmap(tempBm, DEFAULT_COMPRESSION_QUALITY)
                }
            }

            if (isReSizeEnabled) {       //resize image
                resizeImage(tempBm!!)
            }
            return tempBm
        }
    }
}