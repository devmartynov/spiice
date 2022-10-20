package io.devmartynov.spiice

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import io.devmartynov.spiice.databinding.ActivitySignupBinding

class SignUpActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.firstName.doAfterTextChanged {
            handleAfterTextChange(it, AuthAttributes.FIRST_NAME)
        }
        binding.lastName.doAfterTextChanged {
            handleAfterTextChange(it, AuthAttributes.LAST_NAME)
        }
        binding.email.doAfterTextChanged {
            handleAfterTextChange(it, AuthAttributes.EMAIL)
        }
        binding.password.doAfterTextChanged {
            handleAfterTextChange(it, AuthAttributes.PASSWORD)
        }

        binding.signUp.setOnClickListener { sighUp() }
        binding.logIn.setOnClickListener { goToLogin() }
    }

    private fun handleAfterTextChange(editable: Editable?, attribute: AuthAttributes) {
        updateUiErrors(
            validate(editable?.toString() ?: "", attribute), attribute
        )
    }

    private fun updateUiErrors(state: ValidationResult, attribute: AuthAttributes) {
        if (state.hasErrors()) {
            val errors = state.getErrors().joinToString(separator = "\n")

            when (attribute) {
                AuthAttributes.FIRST_NAME -> {
                    binding.firstName.setBackgroundResource(R.drawable.form_field_error_bg)
                    binding.firstNameError.text = errors
                    binding.firstNameError.visibility = View.VISIBLE
                }

                AuthAttributes.LAST_NAME -> {
                    binding.lastName.setBackgroundResource(R.drawable.form_field_error_bg)
                    binding.lastNameError.text = errors
                    binding.lastNameError.visibility = View.VISIBLE
                }

                AuthAttributes.EMAIL -> {
                    binding.email.setBackgroundResource(R.drawable.form_field_error_bg)
                    binding.emailError.text = errors
                    binding.emailError.visibility = View.VISIBLE
                }

                AuthAttributes.PASSWORD -> {
                    binding.password.setBackgroundResource(R.drawable.form_field_error_bg)
                    binding.passwordError.text = errors
                    binding.passwordError.visibility = View.VISIBLE
                }
            }
        } else {
            when (attribute) {
                AuthAttributes.FIRST_NAME -> {
                    binding.firstName.setBackgroundResource(R.drawable.form_field_bg)
                    binding.firstNameError.visibility = View.GONE
                }

                AuthAttributes.LAST_NAME -> {
                    binding.lastName.setBackgroundResource(R.drawable.form_field_bg)
                    binding.lastNameError.visibility = View.GONE
                }

                AuthAttributes.EMAIL -> {
                    binding.email.setBackgroundResource(R.drawable.form_field_bg)
                    binding.emailError.visibility = View.GONE
                }

                AuthAttributes.PASSWORD -> {
                    binding.password.setBackgroundResource(R.drawable.form_field_bg)
                    binding.passwordError.visibility = View.GONE
                }
            }
        }
    }

    private fun sighUp() {
        val firstNameErrors = validate(binding.firstName.text.toString(), AuthAttributes.FIRST_NAME)
        val lastNameNameErrors = validate(binding.lastName.text.toString(), AuthAttributes.LAST_NAME)
        val emailErrors = validate(binding.email.text.toString(), AuthAttributes.EMAIL)
        val passwordErrors = validate(binding.password.toString(), AuthAttributes.PASSWORD)

        updateUiErrors(firstNameErrors, AuthAttributes.FIRST_NAME)
        updateUiErrors(lastNameNameErrors, AuthAttributes.LAST_NAME)
        updateUiErrors(emailErrors, AuthAttributes.EMAIL)
        updateUiErrors(passwordErrors, AuthAttributes.PASSWORD)

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