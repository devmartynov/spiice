package io.devmartynov.spiice.timer

import java.util.*

private const val TIMER_LABEL = "TRANSITION_TIMER"

class TransitionTimer private constructor(private val delay: Long) : Timer {
    private var timer: java.util.Timer? = null

    override fun start(onTimerOver: callback) {
        stop()
        timer = java.util.Timer(TIMER_LABEL, false)
        timer?.schedule(object : TimerTask() {
            override fun run() {
                onTimerOver.invoke()
            }
        }, delay)
    }

    override fun stop() {
        timer?.cancel()
        timer = null
    }

    companion object {
        private var INSTANCE: Timer? = null

        fun initialize(delay: Long) {
            if (INSTANCE == null) {
                INSTANCE = TransitionTimer(delay)
            }
        }

        fun get(): Timer {
            return INSTANCE ?: throw IllegalStateException("TransitionTimer must be initialized")
        }
    }
}