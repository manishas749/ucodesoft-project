package com.logs.permission

import android.content.Context

/**
 * Interface for check runtime permissions.
 */
interface Permission {

    /**
     * For write and read external storage permission.
     * @param context
     */
    fun checkPermission(context: Context)
}