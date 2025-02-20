package com.qa.test.training.utils.manager

import android.os.Handler
import android.os.Looper

class Manager(private val onTimeoutListener: OnTimeoutListener) {
    interface OnTimeoutListener {
        fun onSessionTimeout(lastInteractionTime: Long)
    }

    companion object {
        const val TIMEOUT = 10 * 60 * 1000L // 1 minute
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
    }

    fun resetSession() {
        startSession()
    }

    fun stopSession() {
        runnable?.let {
            handler.removeCallbacks(it)
        }
    }
}
