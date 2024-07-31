package com.example.dmcremalert

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.example.dmcremalert.navigators.AddPermissions
import com.example.dmcremalert.viewmodel.DeviceListViewModel


class Permission(activity: FragmentActivity, var context: Context, private var viewModel: DeviceListViewModel):
    AddPermissions {
    private val BLE_PERMISSIONS = arrayOf<String>(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    private val ANDROID_12_BLE_PERMISSIONS = arrayOf<String>(
        android.Manifest.permission.BLUETOOTH_SCAN,
        android .Manifest.permission.BLUETOOTH_CONNECT,
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.WRITE_EXTERNAL_STORAGE


        )

    override fun requestBlePermissions(activity: Activity?, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) multiplePermissionLauncher!!.launch(
            ANDROID_12_BLE_PERMISSIONS
        ) else multiplePermissionLauncher!!.launch(
            BLE_PERMISSIONS
        )
    }

    private val multiplePermissionsContract: ActivityResultContracts.RequestMultiplePermissions =
        ActivityResultContracts.RequestMultiplePermissions()

    private val multiplePermissionLauncher: ActivityResultLauncher<Array<String>>? =
        activity?.registerForActivityResult(multiplePermissionsContract) { grantResults ->
            val isGranted: Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                java.lang.Boolean.TRUE == grantResults.get(Manifest.permission.ACCESS_FINE_LOCATION) || java.lang.Boolean.TRUE == grantResults.get(
                    Manifest.permission.BLUETOOTH_SCAN
                ) || java.lang.Boolean.TRUE == grantResults.get(Manifest.permission.BLUETOOTH_ADMIN) || java.lang.Boolean.TRUE == grantResults.get(
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            } else {
                java.lang.Boolean.TRUE == grantResults.get(Manifest.permission.ACCESS_FINE_LOCATION) || java.lang.Boolean.TRUE == grantResults.get(
                    Manifest.permission.BLUETOOTH_ADMIN
                )
            }
            if (isGranted) {
                Log.d("granted","granted")
                viewModel.startScanning()
            } else {
                Manifest.permission()
            }
        }

     override fun permission() {
         if (multiplePermissionLauncher != null) {
             askPermissions(multiplePermissionLauncher)
         }
    }

    override fun askPermissions(multiplePermissionLauncher: ActivityResultLauncher<Array<String>>) {
        if (!hasPermissions(ANDROID_12_BLE_PERMISSIONS)) {
            Log.d(
                "PERMISSIONS", "Launching multiple contract permission launcher " +
                        "for ALL required permissions"
            )
            multiplePermissionLauncher.launch(ANDROID_12_BLE_PERMISSIONS)
        } else {
           viewModel.startScanning()
            Log.d("PERMISSIONS", "All permissions are already granted")
        }
    }

   override  fun hasPermissions(permissions: Array<String>?): Boolean {
        if (permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("PERMISSIONS", "Permission is not granted: $permission")
                    return false
                }
               Log.d("PERMISSIONS", "Permission already granted: $permission")
            }
            return true
        }
        return false
    }



}