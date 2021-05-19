package com.loong.ihms.utils

import android.content.Context
import com.loong.ihms.R

class LoadingDialog(context: Context) : BaseDialog(context) {
    init {
        this.setContentView(R.layout.dialog_loading)

        window?.apply {
            this.setBackgroundDrawableResource(R.color.white25)
        }

        maximizeDialogSize()
    }
}