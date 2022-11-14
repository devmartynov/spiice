package io.devmartynov.spiice.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.devmartynov.spiice.utils.FormAttributes
import io.devmartynov.spiice.R
import io.devmartynov.spiice.utils.validation.ValidationResult
import io.devmartynov.spiice.databinding.FragmentLoginBinding
import io.devmartynov.spiice.ui.ViewModelFactory
import io.devmartynov.spiice.ui.notesList.NotesFragment
import io.devmartynov.spiice.utils.text.beautifyListString

/**
 * Экран входа
 */
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by viewModels {
        ViewModelFactory(requireActivity().application)
    }

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

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.visibility = View.GONE

        binding.email.doAfterTextChanged {
            updateUiErrors(viewModel.validateEmail(it.toString()), FormAttributes.EMAIL)
        }
        binding.password.doAfterTextChanged {
            updateUiErrors(viewModel.validatePassword(it.toString()), FormAttributes.PASSWORD)
        }
        binding.logIn.setOnClickListener { signIn() }
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
    private fun signIn() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val emailErrors = viewModel.validateEmail(email)
        val passwordErrors = viewModel.validatePassword(password)

        updateUiErrors(emailErrors, FormAttributes.EMAIL)
        updateUiErrors(passwordErrors, FormAttributes.PASSWORD)

        if (!emailErrors.hasErrors() && !passwordErrors.hasErrors()) {
            val authResult = viewModel.signIn(email, password)
            val authMessage = if (authResult.hasAuthErrors()) {
                beautifyListString(authResult.getAuthErrors())
            } else {
                getString(R.string.auth_successful)
            }

            Toast.makeText(requireContext(), authMessage, Toast.LENGTH_SHORT).show()

            if (!authResult.hasAuthErrors()) {
                goToNoteList()
            }
        }
    }

    /**
     * Переход на экран списка заметок
     */
    private fun goToNoteList() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, NotesFragment())
            .commit()
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