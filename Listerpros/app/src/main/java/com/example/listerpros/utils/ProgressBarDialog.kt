package com.example.listerpros.utils

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import com.example.listerpros.R

class ProgressBarDialog(context: Context) {
    var progressBarDialog= Dialog(context)

    fun showProgressBar(){
        progressBarDialog.setContentView(R.layout.progress_dialog)
        if (progressBarDialog.window != null) {
            progressBarDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        progressBarDialog.show()
    }

    fun hideProgressBar(){
        progressBarDialog.dismiss()
    }
}