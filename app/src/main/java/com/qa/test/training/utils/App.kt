package com.qa.test.training.utils

import android.app.Application
import com.qa.test.training.utils.manager.Manager

class App : Application() {
    lateinit var manager: Manager

    override fun onCreate() {
        super.onCreate()
        // Initialize the Manager here
        manager = Manager(object : Manager.OnTimeoutListener {
            override fun onSessionTimeout(lastInteractionTime: Long) {
                // Handle session timeout here
            }
        })
    }
}
