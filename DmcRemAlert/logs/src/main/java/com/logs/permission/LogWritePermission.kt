package com.logs.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

/**
 * This class is for ask runtime permission for write and read external storage.
 */
class LogWritePermission: Permission  {

    /**
     * For write and read external storage permission.
     * @param context
     */
    override fun checkPermission(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                101
            )
        }else{
            // pdfFileInPrivate()


        }
    }
}