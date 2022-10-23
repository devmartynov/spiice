package io.devmartynov.spiice.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import io.devmartynov.spiice.FormAttributes
import io.devmartynov.spiice.R
import io.devmartynov.spiice.ValidationResult
import io.devmartynov.spiice.databinding.ActivitySignupBinding
import io.devmartynov.spiice.validate

class SignUpActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.firstName.doAfterTextChanged {
            handleAfterTextChange(it, FormAttributes.FIRST_NAME)
        }
        binding.lastName.doAfterTextChanged {
            handleAfterTextChange(it, FormAttributes.LAST_NAME)
        }
        binding.email.doAfterTextChanged {
            handleAfterTextChange(it, FormAttributes.EMAIL)
        }
        binding.password.doAfterTextChanged {
            handleAfterTextChange(it, FormAttributes.PASSWORD)
        }

        binding.signUp.setOnClickListener { sighUp() }
        binding.logIn.setOnClickListener { goToLogin() }
    }

    private fun handleAfterTextChange(editable: Editable?, attribute: FormAttributes) {
        updateUiErrors(
            validate(editable?.toString() ?: "", attribute), attribute
        )
    }

    private fun updateUiErrors(state: ValidationResult, attribute: FormAttributes) {
        if (state.hasErrors()) {
            val errors = state.getErrors().joinToString(separator = "\n")

            when (attribute) {
                FormAttributes.FIRST_NAME -> {
                    binding.firstName.setBackgroundResource(R.drawable.form_field_error_bg)
                    binding.firstNameError.text = errors
                    binding.firstNameError.visibility = View.VISIBLE
                }

                FormAttributes.LAST_NAME -> {
                    binding.lastName.setBackgroundResource(R.drawable.form_field_error_bg)
                    binding.lastNameError.text = errors
                    binding.lastNameError.visibility = View.VISIBLE
                }

                FormAttributes.EMAIL -> {
                    binding.email.setBackgroundResource(R.drawable.form_field_error_bg)
                    binding.emailError.text = errors
                    binding.emailError.visibility = View.VISIBLE
                }

                FormAttributes.PASSWORD -> {
                    binding.password.setBackgroundResource(R.drawable.form_field_error_bg)
                    binding.passwordError.text = errors
                    binding.passwordError.visibility = View.VISIBLE
                }
                else -> {}
            }
        } else {
            when (attribute) {
                FormAttributes.FIRST_NAME -> {
                    binding.firstName.setBackgroundResource(R.drawable.form_field_bg)
                    binding.firstNameError.visibility = View.GONE
                }

                FormAttributes.LAST_NAME -> {
                    binding.lastName.setBackgroundResource(R.drawable.form_field_bg)
                    binding.lastNameError.visibility = View.GONE
                }

                FormAttributes.EMAIL -> {
                    binding.email.setBackgroundResource(R.drawable.form_field_bg)
                    binding.emailError.visibility = View.GONE
                }

                FormAttributes.PASSWORD -> {
                    binding.password.setBackgroundResource(R.drawable.form_field_bg)
                    binding.passwordError.visibility = View.GONE
                }

                else -> {}
            }
        }
    }

    private fun sighUp() {
        val firstNameErrors = validate(binding.firstName.text.toString(), FormAttributes.FIRST_NAME)
        val lastNameNameErrors = validate(binding.lastName.text.toString(), FormAttributes.LAST_NAME)
        val emailErrors = validate(binding.email.text.toString(), FormAttributes.EMAIL)
        val passwordErrors = validate(binding.password.toString(), FormAttributes.PASSWORD)

        updateUiErrors(firstNameErrors, FormAttributes.FIRST_NAME)
        updateUiErrors(lastNameNameErrors, FormAttributes.LAST_NAME)
        updateUiErrors(emailErrors, FormAttributes.EMAIL)
        updateUiErrors(passwordErrors, FormAttributes.PASSWORD)

        if (
            !firstNameErrors.hasErrors()
            && !lastNameNameErrors.hasErrors()
            && !emailErrors.hasErrors()
            && !passwordErrors.hasErrors()
        ) {
            // registration
            Toast.makeText(this, getString(R.string.sign_up_label), Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToLogin() {
        startActivity(
            Intent(this, LoginActivity::class.java)
        )
    }
}