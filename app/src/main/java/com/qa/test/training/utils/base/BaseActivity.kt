package com.qa.test.training.utils.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qa.test.training.features.authentication.LoginActivity
import com.qa.test.training.utils.App
import com.qa.test.training.utils.manager.Manager

abstract class BaseActivity : AppCompatActivity(), Manager.OnTimeoutListener {

    private var isAppInBackground = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as App

        app.manager.onTimeoutListener = this  // Set BaseActivity as the listener
        app.manager.startSession()  // Start the session
    }

    override fun onPause() {
        super.onPause()
        isAppInBackground = true

        // Save the timestamp when the app goes to the background
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        sharedPreferences.edit().putLong("last_background_time", System.currentTimeMillis()).apply()
    }

    override fun onResume() {
        super.onResume()
        isAppInBackground = false

        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val lastBackgroundTime = sharedPreferences.getLong("last_background_time", 0)

        if (lastBackgroundTime > 0) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastBackgroundTime >= Manager.TIMEOUT) {
                // If the app was in the background for too long, log out the user
                redirectToLogin()
                return
            }
        }

        // Restart session
        (application as App).manager.startSession()
    }

    // Detect when the app goes into the background and log out if necessary
    override fun onUserInteraction() {
        super.onUserInteraction()
        (application as App).manager.onUserInteraction()
    }

    override fun onSessionTimeout(lastInteractionTime: Long) {
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("is_logged_in", false).apply()

        redirectToLogin()  // Ensure all activities log out properly
    }

    protected fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}
