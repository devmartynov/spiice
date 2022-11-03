package io.devmartynov.spiice

import android.app.Application
import io.devmartynov.spiice.utils.timer.TransitionTimer

class SliiceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TransitionTimer.initialize(5000)
    }
}