package com.qa.test.training.features.calculator

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import com.qa.test.training.R
import com.qa.test.training.features.authentication.LoginActivity
import com.qa.test.training.utils.base.BaseActivity
import com.qa.test.training.utils.manager.Manager
import java.util.Locale

class CalculatorActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, Manager.OnTimeoutListener {

    private lateinit var manager: Manager

    private lateinit var buttons: Map<Int, Button>
    private lateinit var inputDisplay: TextView
    private lateinit var outputDisplay: TextView

    private var valueOne = Double.NaN
    private var valueTwo = 0.0
    private var currentAction = NONE

    companion object {
        private const val ADDITION = '+'
        private const val SUBTRACTION = '-'
        private const val MULTIPLICATION = '*'
        private const val DIVISION = '/'
        private const val PERCENT = '%'
        private const val NONE = ' '
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)
        // Initialize manager and set the timeout listener
        manager = Manager(this)

        initializeViews()
        setButtonListeners()
    }

    private fun initializeViews() {
        buttons = mapOf(
            R.id.button0 to findViewById(R.id.button0),
            R.id.button1 to findViewById(R.id.button1),
            R.id.button2 to findViewById(R.id.button2),
            R.id.button3 to findViewById(R.id.button3),
            R.id.button4 to findViewById(R.id.button4),
            R.id.button5 to findViewById(R.id.button5),
            R.id.button6 to findViewById(R.id.button6),
            R.id.button7 to findViewById(R.id.button7),
            R.id.button8 to findViewById(R.id.button8),
            R.id.button9 to findViewById(R.id.button9),
            R.id.button_add to findViewById(R.id.button_add),
            R.id.button_sub to findViewById(R.id.button_sub),
            R.id.button_multi to findViewById(R.id.button_multi),
            R.id.button_divide to findViewById(R.id.button_divide),
            R.id.button_percent to findViewById(R.id.button_percent),
            R.id.button_positive_negative to findViewById(R.id.button_positive_negative),
            R.id.button_dot to findViewById(R.id.button_dot),
            R.id.button_clear to findViewById(R.id.button_clear),
            R.id.button_equal to findViewById(R.id.button_equal)
        )

        inputDisplay = findViewById(R.id.input)
        outputDisplay = findViewById(R.id.output)
    }

    private fun setButtonListeners() {
        buttons.forEach { (id, button) ->
            button.setOnClickListener { handleButtonClick(id) }
        }
    }

    private fun handleButtonClick(buttonId: Int) {
        when (buttonId) {
            R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8, R.id.button9 -> {
                appendToInput(buttons[buttonId]?.text.toString())
            }

            R.id.button_add -> prepareOperation(ADDITION)
            R.id.button_sub -> prepareOperation(SUBTRACTION)
            R.id.button_multi -> prepareOperation(MULTIPLICATION)
            R.id.button_divide -> prepareOperation(DIVISION)
            R.id.button_percent -> prepareOperation(PERCENT)
            R.id.button_positive_negative -> toggleSign()  // Added logic for +/- button
            R.id.button_dot -> appendToInput(".")
            R.id.button_clear -> clearInput()
            R.id.button_equal -> calculateResult()
        }
    }

    private fun appendToInput(value: String) {
        val currentText = inputDisplay.text.toString()
        inputDisplay.text = getString(R.string.concat_text_placeholder, currentText, value)
        adjustFontSize()
    }

    private fun prepareOperation(action: Char) {
        if (!valueOne.isNaN()) {
            valueTwo = parseInput()
            performOperation()
        } else {
            valueOne = parseInput()
        }

        currentAction = action
        val resultText = getString(R.string.operation_display, formatValue(valueOne), action)
        outputDisplay.text = resultText
        inputDisplay.text = ""
    }

    private fun calculateResult() {
        if (currentAction != NONE) {
            valueTwo = parseInput()

            // Make sure the operation is valid and not dividing by zero
            if (currentAction == DIVISION && valueTwo == 0.0) {
                outputDisplay.text = getString(R.string.error_divide_by_zero)
            } else {
                performOperation()
                outputDisplay.text = formatValue(valueOne)
                inputDisplay.text = ""
                currentAction = NONE
            }
        }
    }

    private fun clearInput() {
        inputDisplay.text = ""
        outputDisplay.text = ""
        valueOne = Double.NaN
        valueTwo = 0.0
        currentAction = NONE
    }

    private fun performOperation() {
        valueOne = when (currentAction) {
            ADDITION -> valueOne + valueTwo
            SUBTRACTION -> valueOne - valueTwo
            MULTIPLICATION -> valueOne * valueTwo
            DIVISION -> if (valueTwo != 0.0) valueOne / valueTwo else Double.NaN
            PERCENT -> valueOne * valueTwo / 100
            else -> valueOne
        }
    }

    private fun parseInput(): Double {
        val input = inputDisplay.text.toString()
        return input.toDoubleOrNull() ?: 0.0
    }

    private fun formatValue(value: Double): String {
        return if (value == value.toLong().toDouble()) {
            value.toLong().toString()
        } else {
            value.toString()
        }
    }

    // Adding more checks for the input size
    private fun adjustFontSize() {
        val textLength = inputDisplay.text.toString().length
        val newSize = when {
            textLength > 15 -> 20f
            textLength > 10 -> 25f
            else -> 30f
        }
        inputDisplay.setTextSize(TypedValue.COMPLEX_UNIT_SP, newSize)
    }

    private fun toggleSign() {
        val currentText = inputDisplay.text.toString()
        if (currentText.isNotEmpty()) {
            val currentValue = currentText.toDoubleOrNull() ?: return
            val newValue = -currentValue

            // Format the number using the localized string resource for %.2f
            val formattedValue =
                String.format(Locale.getDefault(), "%.2f", newValue).trimEnd('0').trimEnd('.')

            // Update the inputDisplay text using the concat_text_placeholder
            inputDisplay.text = getString(R.string.concat_text_placeholder, "", formattedValue)
            adjustFontSize()
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
        finish()  // Optionally finish HomeActivity    }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("Not yet implemented")
    }
}
