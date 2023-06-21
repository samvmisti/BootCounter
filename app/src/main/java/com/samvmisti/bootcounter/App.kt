package com.samvmisti.bootcounter

import android.app.Application
import com.samvmisti.bootcounter.di.startDI

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startDI(this)
    }
}