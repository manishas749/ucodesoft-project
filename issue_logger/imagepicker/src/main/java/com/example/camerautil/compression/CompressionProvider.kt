package com.example.camerautil.compression

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.IntRange
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class CompressionProvider {
    companion object {

        // compress quality of image upto given quality
        fun compressBitmap(bitmap: Bitmap?, @IntRange(from = 0, to = 100) quality: Int): Bitmap? {
            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos) // 100baos

            var options = 100
            while (baos.toByteArray().size / 1024 > 100 && (options in quality..100)) {
                baos.reset() // baosbaos
                bitmap?.compress(Bitmap.CompressFormat.JPEG, options, baos) // options%baos
                options -= 10 // 10
            }
            val isBm = ByteArrayInputStream(
                baos.toByteArray()
            ) // baosByteArrayInputStream

            return BitmapFactory.decodeStream(isBm, null, null)
        }

        // resize the image
        fun resizeImage(image: Bitmap): Bitmap? {
            val width = image.width
            val height = image.height

            val scaleWidth = width - 10
            val scaleHeight = height - 10

            if (image.byteCount <= 100000)
                return image

            return Bitmap.createScaledBitmap(image, scaleWidth, scaleHeight, false)
        }
    }
}