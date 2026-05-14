package com.example.training_project.ui.base

import android.app.Dialog
import android.content.Context
import com.example.training_project.R

class LoadingDialog(context: Context) : Dialog(context) {
    init {
        setContentView(R.layout.layout_loading_dialog)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(false)
    }
    fun IfNotShowing() {
        if (!isShowing) show()
    }
}