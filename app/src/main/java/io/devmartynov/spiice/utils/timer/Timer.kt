package io.devmartynov.spiice.utils.timer

typealias callback = () -> Unit

interface Timer {
    fun start(onTimerOver: callback)

    fun stop()
}