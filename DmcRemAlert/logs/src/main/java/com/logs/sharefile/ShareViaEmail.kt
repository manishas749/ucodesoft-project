package com.logs.sharefile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.logs.Constants
import com.logs.R
import java.io.File

/**
 * This class is used to send log file through email.
 */
class ShareViaEmail : ShareFile {

    /**
     * Function used for send email.
     * @param context
     * @param provider string
     */
    override fun sendEmail(
        context: Context,
        provider: String
    ) {
        val emailTo: String? = null

        val path: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            (context as Activity).externalMediaDirs.firstOrNull()!!.path
        } else {
            Environment.getExternalStorageDirectory().toString() +"/"+ Constants.folderName
        }
        Log.d("checkFilePath", path)
        val file = File(path, Constants.fileName)
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = context.getString(R.string.emailIntentTypeDir)
        emailIntent.type = context.getString(R.string.emailIntentTypeFile)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailTo))
        emailIntent.putExtra(
            Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                context,
                provider,
                file
            )
        )
        emailIntent.putExtra(
            Intent.EXTRA_SUBJECT,
            context.getString(R.string.loggerFile)
        )
        emailIntent.putExtra(
            Intent.EXTRA_TEXT, context.getString(R.string.hello) +
                    "\n" +
                    context.getString(R.string.emailAttachment)
        )
        context.startActivity(
            Intent.createChooser(
                emailIntent,
                context.getString(R.string.sendingEmail)
            )
        )
    }
}