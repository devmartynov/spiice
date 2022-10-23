package io.devmartynov.spiice.timer

typealias callback = () -> Unit

interface Timer {
    fun start(onTimerOver: callback)

    fun stop()
}