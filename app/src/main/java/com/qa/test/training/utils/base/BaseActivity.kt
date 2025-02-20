package com.qa.test.training.utils.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qa.test.training.utils.App

abstract class BaseActivity : AppCompatActivity() {

    private var isAppInBackground = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val manager = (application as App).manager
        manager.startSession()  // Start the session when the activity is created
    }

    override fun onResume() {
        super.onResume()
        isAppInBackground = false
        (application as App).manager.startSession()  // Restart the session when the app returns to the foreground
    }

    override fun onPause() {
        super.onPause()
        isAppInBackground = true
        (application as App).manager.stopSession()  // Stop the session when the app goes into the background
    }

    // Detect when the app goes into the background and log out if necessary
    override fun onUserInteraction() {
        super.onUserInteraction()
        (application as App).manager.onUserInteraction()
        if (isAppInBackground) {
            (application as App).manager.resetSession()  // Reset session if app is in background
        }
    }
}
