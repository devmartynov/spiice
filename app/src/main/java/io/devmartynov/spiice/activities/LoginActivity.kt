package io.devmartynov.spiice.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import io.devmartynov.spiice.FormAttributes
import io.devmartynov.spiice.R
import io.devmartynov.spiice.ValidationResult
import io.devmartynov.spiice.databinding.ActivityLoginBinding
import io.devmartynov.spiice.validate

class LoginActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.email.doAfterTextChanged {
            updateUiErrors(
                validate(it.toString(), FormAttributes.EMAIL), FormAttributes.EMAIL
            )
        }

        binding.password.doAfterTextChanged {
            updateUiErrors(
                validate(it.toString(), FormAttributes.PASSWORD), FormAttributes.PASSWORD
            )
        }

        binding.logIn.setOnClickListener { login() }
        binding.signUp.setOnClickListener { goToSignUp() }
    }

    private fun updateUiErrors(state: ValidationResult, attribute: FormAttributes) {
        if (state.hasErrors()) {
            val errors = state.getErrors().joinToString(separator = "\n")

            if (attribute == FormAttributes.EMAIL) {
                binding.email.setBackgroundResource(R.drawable.form_field_error_bg)
                binding.emailError.text = errors
                binding.emailError.visibility = View.VISIBLE
            } else {
                binding.password.setBackgroundResource(R.drawable.form_field_error_bg)
                binding.passwordError.text = errors
                binding.passwordError.visibility = View.VISIBLE
            }
        } else {
            if (attribute == FormAttributes.EMAIL) {
                binding.email.setBackgroundResource(R.drawable.form_field_bg)
                binding.emailError.visibility = View.GONE
            } else {
                binding.password.setBackgroundResource(R.drawable.form_field_bg)
                binding.passwordError.visibility = View.GONE
            }
        }
    }

    private fun login() {
        val emailErrors = validate(binding.email.text.toString(), FormAttributes.EMAIL)
        val passwordErrors = validate(binding.password.text.toString(), FormAttributes.PASSWORD)

        updateUiErrors(emailErrors, FormAttributes.EMAIL)
        updateUiErrors(passwordErrors, FormAttributes.PASSWORD)

        if (!emailErrors.hasErrors() && !passwordErrors.hasErrors()) {
            startActivity(
                Intent(this@LoginActivity, NotesActivity::class.java)
            )
        }
    }

    private fun goToSignUp() {
        startActivity(
            Intent(this, SignUpActivity::class.java)
        )
    }
}