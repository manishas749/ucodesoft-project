package com.example.camerautil.listener

import android.app.Dialog
import android.content.Intent
import com.example.camerautil.provider.RequestCodeProvider

interface ChooserClickListener {
    fun onClick(reqCode: RequestCodeProvider, intent: Intent) {}
    fun customDialogLayout(dialog: Dialog) {}
}