package com.qa.test.training.features.authentication

<<<<<<< Updated upstream
<<<<<<< Updated upstream
class LoginActivity {
}
=======
=======
>>>>>>> Stashed changes
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.qa.test.training.R
import com.qa.test.training.features.calculator.CalculatorActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        usernameEditText = findViewById(R.id.usernameEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)

        // Set login button listener
        loginButton.setOnClickListener {
            // Validate the credentials (this is just a simple example, adjust as needed)
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Check for empty fields
            if (username.isEmpty()) {
                usernameEditText.error = "Username is required"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                passwordEditText.error = "Password is required"
                return@setOnClickListener
            }


            if (username == "admin" && password == "1234") {
                // Successfully logged in, move to the MainActivity (calculator)
                val intent = Intent(this, CalculatorActivity::class.java)
                startActivity(intent)
                finish() // Close the LoginActivity so the user can't go back to it
            } else {
                // Handle invalid login (you can add an error message or alert)
                passwordEditText.error = "Invalid username or password"
            }
        }
    }
}
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
