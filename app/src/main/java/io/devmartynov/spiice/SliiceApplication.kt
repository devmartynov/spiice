package io.devmartynov.spiice

import android.app.Application

class SliiceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TransitionTimer.initialize(5000)
    }
}