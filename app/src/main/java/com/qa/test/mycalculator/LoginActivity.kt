package com.qa.test.mycalculator

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.login_button)

        // Set login button listener
        loginButton.setOnClickListener {
            // Validate the credentials (this is just a simple example, adjust as needed)
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username == "admin" && password == "1234") {
                // Successfully logged in, move to the MainActivity (calculator)
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Close the LoginActivity so the user can't go back to it
            } else {
                // Handle invalid login (you can add an error message or alert)
                passwordEditText.error = "Invalid username or password"
            }
        }
    }
}
