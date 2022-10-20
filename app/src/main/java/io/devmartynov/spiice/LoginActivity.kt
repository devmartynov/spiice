package io.devmartynov.spiice

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import io.devmartynov.spiice.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.email.doAfterTextChanged {
            updateUiErrors(
                validate(it.toString(), AuthAttributes.EMAIL),
                AuthAttributes.EMAIL
            )
        }

        binding.password.doAfterTextChanged {
            updateUiErrors(
                validate(it.toString(), AuthAttributes.PASSWORD),
                AuthAttributes.PASSWORD
            )
        }

        binding.logIn.setOnClickListener { login() }
        binding.signUp.setOnClickListener { goToSignUp() }
    }

    private fun updateUiErrors(state: ValidationResult, attribute: AuthAttributes) {
        if (state.hasErrors()) {
            val errors = state.getErrors().joinToString(separator = "\n")

            if (attribute == AuthAttributes.EMAIL) {
                binding.email.setBackgroundResource(R.drawable.form_field_error_bg)
                binding.emailError.text = errors
                binding.emailError.visibility = View.VISIBLE
            } else {
                binding.password.setBackgroundResource(R.drawable.form_field_error_bg)
                binding.passwordError.text = errors
                binding.passwordError.visibility = View.VISIBLE
            }
        } else {
            if (attribute == AuthAttributes.EMAIL) {
                binding.email.setBackgroundResource(R.drawable.form_field_bg)
                binding.emailError.visibility = View.GONE
            } else {
                binding.password.setBackgroundResource(R.drawable.form_field_bg)
                binding.passwordError.visibility = View.GONE
            }
        }
    }

    private fun login() {
        val emailErrors = validate(binding.email.text.toString(), AuthAttributes.EMAIL)
        val passwordErrors = validate(binding.password.text.toString(), AuthAttributes.PASSWORD)

        updateUiErrors(emailErrors, AuthAttributes.EMAIL)
        updateUiErrors(passwordErrors, AuthAttributes.PASSWORD)

        if (!emailErrors.hasErrors() && !passwordErrors.hasErrors()) {
            // authorization
            Toast.makeText(this, getString(R.string.log_in_label), Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToSignUp() {
        startActivity(
            Intent(this, SignUpActivity::class.java)
        )
    }
}