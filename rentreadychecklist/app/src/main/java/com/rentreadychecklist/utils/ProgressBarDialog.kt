package com.rentreadychecklist.utils

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import com.rentreadychecklist.R


/**
 * This class for show progress bar.
 */
class ProgressBarDialog(context: Context) {
    private var progressBarDialog = Dialog(context)

    fun showProgressBar(): Boolean {
        progressBarDialog.setContentView(R.layout.progress_dialog)
        if (progressBarDialog.window != null) {
            progressBarDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            progressBarDialog.setCancelable(false)
        }
        progressBarDialog.show()
        return true
    }

    fun hideProgressBar(): Boolean {
        progressBarDialog.dismiss()
        return true
    }
}