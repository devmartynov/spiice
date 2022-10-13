package io.devmartynov.spiice

typealias callback = () -> Unit

interface Timer {
    fun start(onTimerOver: callback)

    fun stop()
}