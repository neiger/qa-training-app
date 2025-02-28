package com.qa.test.training.features.authentication

import android.os.Bundle
import android.widget.Toast
import com.qa.test.training.utils.auth.UserManager
import com.qa.test.training.utils.base.BaseActivity
import com.qa.test.training.databinding.ActivityRegisterBinding

class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userManager = UserManager(this)

        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val username = binding.usernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userManager.saveUser(name, username, password)
            Toast.makeText(this, "User registered successfully!", Toast.LENGTH_SHORT).show()
            finish() // Return to LoginActivity
        }
    }
}
