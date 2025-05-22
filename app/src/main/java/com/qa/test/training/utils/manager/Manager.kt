package com.qa.test.training.utils.manager

import android.os.Handler
import android.os.Looper

class Manager(var onTimeoutListener: OnTimeoutListener) {
    interface OnTimeoutListener {
        fun onSessionTimeout(lastInteractionTime: Long)
    }

    companion object {
        const val TIMEOUT = 5 * 60 * 1000L // 1 minute
    }

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    var lastInteractionTime: Long = System.currentTimeMillis()

    fun startSession() {
        stopSession()
        runnable = Runnable { onTimeoutListener.onSessionTimeout(lastInteractionTime) }
        handler.postDelayed(runnable!!, TIMEOUT)
    }

    fun onUserInteraction() {
        lastInteractionTime = System.currentTimeMillis()
        startSession()  // Restart the session timer on user interaction
    }

    private fun stopSession() {
        runnable?.let {
            handler.removeCallbacks(it)
        }
    }
}
