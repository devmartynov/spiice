package io.devmartynov.spiice

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.devmartynov.spiice.utils.timer.TransitionTimer

@HiltAndroidApp
class SliiceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TransitionTimer.initialize(5000)
    }
}