package com.loong.ihms.utils

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.loong.ihms.R
import timber.log.Timber

open class BaseDialog constructor(context: Context, style: Int = R.style.BaseDialogTheme) : Dialog(context, style) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        window?.apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)

            this.setBackgroundDrawableResource(R.color.transparent)
            this.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
    }

    fun maximizeDialogWidth() {
        window?.apply {
            this.setLayout(context.screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    fun maximizeDialogSize() {
        window?.apply {
            this.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    override fun show() {
        try {
            super.show()
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}