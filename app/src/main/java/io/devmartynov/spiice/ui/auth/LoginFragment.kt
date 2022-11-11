package io.devmartynov.spiice.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import io.devmartynov.spiice.utils.FormAttributes
import io.devmartynov.spiice.R
import io.devmartynov.spiice.utils.validation.ValidationResult
import io.devmartynov.spiice.databinding.FragmentLoginBinding
import io.devmartynov.spiice.ui.notesList.NotesFragment
import io.devmartynov.spiice.validate

/**
 * Экран входа
 */
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    /**
     * Обновляет UI, добавляя или убриая ошибки валидации
     * @param state результат валидации
     * @param attribute атрибут поля ввода
     */
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

    /**
     * Авторизация пользователя.
     * Перед авторизацией поля валидируются. В случае возникновения ошибок авторизации не происходит.
     */
    private fun login() {
        val emailErrors = validate(binding.email.text.toString(), FormAttributes.EMAIL)
        val passwordErrors = validate(binding.password.text.toString(), FormAttributes.PASSWORD)

        updateUiErrors(emailErrors, FormAttributes.EMAIL)
        updateUiErrors(passwordErrors, FormAttributes.PASSWORD)

        if (!emailErrors.hasErrors() && !passwordErrors.hasErrors()) {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, NotesFragment())
                .commit()
        }
    }

    /**
     * Переход на экран регистрации
     */
    private fun goToSignUp() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, SignUpFragment())
            .commit()
    }
}