package com.example.camerautil.gallery

import android.content.Intent
import android.content.Intent.createChooser

/***
 *
 * GalleryProvider class
 *      * provider gallery related functions for user
 *
 ***/

class GalleryProvider {
    fun singleImageGalleryIntent(): Intent = Intent().apply {
        type = "image/*"
        action = Intent.ACTION_GET_CONTENT
    }

    fun multipleImageGalleryIntent(): Intent = Intent().apply {
        type = "image/*"
        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        action = Intent.ACTION_GET_CONTENT
        createChooser(this, "Select Pictures")
    }
}