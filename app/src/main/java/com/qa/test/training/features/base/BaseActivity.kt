package com.qa.test.training.features.base

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.qa.test.training.features.authentication.LoginActivity


open class BaseActivity : AppCompatActivity() {

    private val inactivityTimeout: Long = 1 * 60 * 1000 // 5 minutes of inactivity
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onResume() {
        super.onResume()
        // Reset inactivity timer when the activity is resumed
        startInactivityTimer()
    }

    override fun onPause() {
        super.onPause()
        // Remove any existing inactivity handler if the activity is paused
        handler.removeCallbacks(runnable)
    }

    private fun startInactivityTimer() {
        runnable = Runnable {
            // If inactivity timeout is reached, redirect to login
            redirectToLogin()
        }
        handler.postDelayed(runnable, inactivityTimeout)
    }

    protected fun redirectToLogin() {
        // Create an intent to redirect to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()  // Ensure that the current activity is finished
    }
}
