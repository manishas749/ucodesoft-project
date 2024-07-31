package com.example.dmcremalert.navigators

import android.app.Activity
import androidx.activity.result.ActivityResultLauncher

interface AddPermissions {

    fun requestBlePermissions(activity: Activity?, requestCode: Int)
    fun permission()
    fun askPermissions(multiplePermissionLauncher: ActivityResultLauncher<Array<String>>)
    fun  hasPermissions(permissions: Array<String>?): Boolean

}