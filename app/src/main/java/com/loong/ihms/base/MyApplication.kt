package com.loong.ihms.base

import android.app.Application
import com.loong.ihms.BuildConfig
import com.loong.ihms.utils.LocalStorageUtil
import timber.log.Timber
import timber.log.Timber.DebugTree

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Local storage init
        LocalStorageUtil.initialize(this)

        if (BuildConfig.DEBUG) {
            // Timber init
            Timber.plant(DebugTree())
        }
    }
}