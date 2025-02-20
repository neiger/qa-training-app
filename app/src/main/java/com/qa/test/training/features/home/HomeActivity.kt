package com.qa.test.training.features.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.addCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.qa.test.training.R
import com.qa.test.training.databinding.ActivityHomeBinding
import com.qa.test.training.features.calculator.CalculatorActivity
import com.qa.test.training.features.authentication.LoginActivity
import com.qa.test.training.utils.base.BaseActivity
import com.qa.test.training.utils.manager.Manager

class HomeActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, Manager.OnTimeoutListener {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var manager: Manager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize manager and set the timeout listener
        manager = Manager(this)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navView.setNavigationItemSelectedListener(this)

        // Handling back press using OnBackPressedCallback
        onBackPressedDispatcher.addCallback(this) {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                isEnabled = false // Disable the callback to let the activity finish
                finish() // Close the activity (equivalent to onBackPressed)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Restart session when activity is in foreground
        manager.startSession()
    }

    override fun onPause() {
        super.onPause()
        // Stop session when activity goes to background
        manager.stopSession()
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        // Reset the session on user interaction
        manager.onUserInteraction()
    }

    override fun onSessionTimeout(lastInteractionTime: Long) {
        // Log out the user if the session times out
        val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("is_logged_in", false).apply()

        // Redirect to LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()  // Optionally finish HomeActivity
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_calculator -> {
                // Start CalculatorActivity
                startActivity(Intent(this, CalculatorActivity::class.java))
            }
            R.id.nav_logout -> {
                // Clear login session
                val sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)
                sharedPreferences.edit().putBoolean("is_logged_in", false).apply()

                // Log out and redirect to LoginActivity
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
