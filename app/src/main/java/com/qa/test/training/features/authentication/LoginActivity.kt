package com.qa.test.training.features.authentication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.qa.test.training.utils.base.BaseActivity
import com.qa.test.training.features.home.HomeActivity
import com.qa.test.training.databinding.ActivityLoginBinding
import com.qa.test.training.utils.auth.UserManager

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userManager: UserManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userManager = UserManager(this)

        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE)

        // Check if already logged in
        if (sharedPreferences.getBoolean("is_logged_in", false)) {
            // If session exists, move directly to HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set login button listener
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            // Validate inputs
            if (username.isEmpty()) {
                binding.usernameEditText.error = "Username is required"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.passwordEditText.error = "Password is required"
                return@setOnClickListener
            }

            // Check login credentials
            if (userManager.isValidUser(username, password)) {
                userManager.setLoggedIn(true)
                navigateToHome()
            } else {
                binding.passwordEditText.error = "Invalid username or password"
            }
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
