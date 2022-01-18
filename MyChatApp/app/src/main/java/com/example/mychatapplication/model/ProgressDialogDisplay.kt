package com.example.mychatapplication.model

import android.app.ProgressDialog
import android.content.Context

@Suppress("DEPRECATION")
class ProgressDialogDisplay {

    private lateinit var progress: ProgressDialog
    fun progressDialogShow(message: String, context: Context) {
        progress = ProgressDialog(context)
        progress.setMessage(message)
        progress.secondaryProgress = ProgressDialog.STYLE_SPINNER
        progress.isIndeterminate = true
        progress.progress = 0
        progress.show()
    }
    fun progressDialogDismiss(context: Context){
        progress = ProgressDialog(context)
        progress.dismiss()
    }
}