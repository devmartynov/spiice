package io.devmartynov.spiice.ui.auth

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import io.devmartynov.spiice.utils.FormAttributes
import io.devmartynov.spiice.R
import io.devmartynov.spiice.utils.validation.ValidationResult
import io.devmartynov.spiice.databinding.FragmentSignupBinding
import io.devmartynov.spiice.validate

/**
 * Экран регистрации
 */
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    /**
     * Обработчик события, которые вызывается после изменения текста в поле ввод
     * @param editable поле ввода
     * @param attribute атрибут поля ввода
     */
    private fun handleAfterTextChange(editable: Editable?, attribute: FormAttributes) {
        updateUiErrors(
            validate(editable?.toString() ?: "", attribute), attribute
        )
    }

    /**
     * Обновляет UI, добавляя или убриая ошибки валидации
     * @param state результат валидации
     * @param attribute атрибут поля ввода
     */
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

    /**
     * Регистрация пользователя.
     * Перед регистрацией поля валидируются. В случае возникновения ошибок регистрации не происходит.
     */
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
            Toast.makeText(requireContext(), getString(R.string.sign_up_label), Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Переход на экран авторизации
     */
    private fun goToLogin() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, LoginFragment())
            .commit()
    }
}