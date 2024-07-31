package com.rentreadychecklist.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.rentreadychecklist.R

/**
 * This class used for check and ask permission, when user denied permission.
 */
class CheckPermission(private val context: Context) : AppCompatActivity() {

    private fun goToSettings() {
        val myAppSettings = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse(
                context.resources.getString(R.string.packageName) +
                        context.resources.getString(R.string.com_rentreadychecklist)
            )
        )
        myAppSettings.addCategory(Intent.CATEGORY_DEFAULT)
        myAppSettings.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(myAppSettings)
    }

    private fun showPermissionDeniedDialog(message: String) {
        AlertDialog.Builder(context)
            .setTitle(context.resources.getString(R.string.permissionRequired))
            .setMessage(message)
            .setPositiveButton(
                context.resources.getString(R.string.action_settings)
            ) { _, _ ->
                // send to app settings if permission is denied permanently
                goToSettings()
            }
            .setNegativeButton(context.resources.getString(R.string.cancel), null)
            .setCancelable(false)
            .show()
    }





    // Show message in case permission not granted for files and media.
    fun showStoragePermissionDialog() {
        showPermissionDeniedDialog(context.resources.getString(R.string.storagePermission))
    }

    // Show message in case permission not granted for camera.
    fun showCameraAndGalleryPermissionDialog() {
        showPermissionDeniedDialog(context.resources.getString(R.string.cameraPermission))
    }

}