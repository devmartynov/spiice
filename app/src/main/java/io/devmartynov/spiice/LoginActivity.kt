package io.devmartynov.spiice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged

class LoginActivity: AppCompatActivity() {
    private lateinit var emailField: EditText
    private lateinit var emailErrorText: TextView
    private lateinit var passwordField: EditText
    private lateinit var passwordErrorText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailField = findViewById(R.id.email)
        emailErrorText = findViewById(R.id.email_error)
        passwordField = findViewById(R.id.password)
        passwordErrorText = findViewById(R.id.password_error)

        emailField.doAfterTextChanged {
            updateUiErrors(
                validate(it.toString(), AuthAttributes.EMAIL),
                AuthAttributes.EMAIL
            )
        }

        passwordField.doAfterTextChanged {
            updateUiErrors(
                validate(it.toString(), AuthAttributes.PASSWORD),
                AuthAttributes.PASSWORD
            )
        }

        findViewById<Button>(R.id.log_in).setOnClickListener { login() }
        findViewById<Button>(R.id.sign_up).setOnClickListener { goToSignUp() }
    }

    private fun updateUiErrors(state: ValidationResult, attribute: AuthAttributes) {
        if (state.hasErrors()) {
            val errors = state.getErrors().joinToString(separator = "\n")

            if (attribute == AuthAttributes.EMAIL) {
                emailField.setBackgroundResource(R.drawable.form_field_error_bg)
                emailErrorText.text = errors
                emailErrorText.visibility = View.VISIBLE
            } else {
                passwordField.setBackgroundResource(R.drawable.form_field_error_bg)
                passwordErrorText.text = errors
                passwordErrorText.visibility = View.VISIBLE
            }
        } else {
            if (attribute == AuthAttributes.EMAIL) {
                emailField.setBackgroundResource(R.drawable.form_field_bg)
                emailErrorText.visibility = View.GONE
            } else {
                passwordField.setBackgroundResource(R.drawable.form_field_bg)
                passwordErrorText.visibility = View.GONE
            }
        }
    }

    private fun login() {
        val emailErrors = validate(emailField.text.toString(), AuthAttributes.EMAIL)
        val passwordErrors = validate(passwordField.text.toString(), AuthAttributes.PASSWORD)

        updateUiErrors(emailErrors, AuthAttributes.EMAIL)
        updateUiErrors(passwordErrors, AuthAttributes.PASSWORD)

        if (!emailErrors.hasErrors() && !passwordErrors.hasErrors()) { // authorization
            Toast.makeText(this, getString(R.string.log_in_label), Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToSignUp() {
        startActivity(
            Intent(this, SignUpActivity::class.java)
        )
    }
}