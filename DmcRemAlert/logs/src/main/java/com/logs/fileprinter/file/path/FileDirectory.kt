package com.logs.fileprinter.file.path

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Environment
import java.io.File

/**
 * This class used for get directory of a folder for log file.
 */
class FileDirectory(private val context: Context): FilePath {


    /**
     * This function will return LOG folder path.
     * @return string
     */
    override fun getFilePath(): String {
        val folderName = "LOG"
        val directory: File = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val mediaDir = (context as Activity).externalMediaDirs.firstOrNull()

            if (mediaDir != null && mediaDir.exists()) {
                mediaDir
            } else {
                context.filesDir
            }
        } else {
            File(Environment.getExternalStorageDirectory(), "/$folderName")
        }
        if (!directory.exists()) {
            directory.mkdir()
        }

        return directory.path
    }
}