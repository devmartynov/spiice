package io.devmartynov.spiice.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import io.devmartynov.spiice.utils.FormAttributes
import io.devmartynov.spiice.R
import io.devmartynov.spiice.utils.validation.ValidationResult
import io.devmartynov.spiice.databinding.FragmentSignupBinding
import io.devmartynov.spiice.utils.asyncOperationState.AsyncOperationState
import io.devmartynov.spiice.utils.text.beautifyListString

/**
 * Экран регистрации
 */
@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val viewModel: AuthViewModel by viewModels()
    private var bottomNav: BottomNavigationView? = null

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

        viewModel.authState.observe(viewLifecycleOwner) { authState ->
            when (authState) {
                is AsyncOperationState.Loading -> {
                }
                is AsyncOperationState.Success -> {
                    val authResult = authState.data as AuthResult
                    val authMessage = if (authResult.hasAuthErrors()) {
                        beautifyListString(authResult.getAuthErrors())
                    } else {
                        getString(R.string.auth_successful)
                    }

                    Toast.makeText(requireContext(), authMessage, Toast.LENGTH_SHORT).show()

                    if (!authResult.hasAuthErrors()) {
                        findNavController().navigate(
                            SignUpFragmentDirections.actionSignUpFragmentToNotesFragment()
                        )
                    }
                }
                is AsyncOperationState.Failure -> {
                    Toast.makeText(requireContext(), getString(R.string.failure_note_creation_message), Toast.LENGTH_SHORT).show()
                }
                is AsyncOperationState.Idle -> {
                }
            }
        }
        bottomNav = requireActivity().findViewById(R.id.bottom_navigation)
        bottomNav?.visibility = View.GONE

        binding.firstName.doAfterTextChanged {
            updateUiErrors(viewModel.validateFirstName(it.toString()), FormAttributes.FIRST_NAME)
        }
        binding.lastName.doAfterTextChanged {
            updateUiErrors(viewModel.validateLastName(it.toString()), FormAttributes.LAST_NAME)
        }
        binding.email.doAfterTextChanged {
            updateUiErrors(viewModel.validateEmail(it.toString()), FormAttributes.EMAIL)
        }
        binding.password.doAfterTextChanged {
            updateUiErrors(viewModel.validatePassword(it.toString()), FormAttributes.PASSWORD)
        }
        binding.signUp.setOnClickListener { sighUp() }
        binding.logIn.setOnClickListener {
            findNavController().navigate(
                SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
            )
        }
    }

    /**
     * Обновляет UI, добавляя или убриая ошибки валидации
     * @param state результат валидации
     * @param attribute атрибут поля ввода
     */
    private fun updateUiErrors(state: ValidationResult, attribute: FormAttributes) {
        if (state.hasErrors()) {
            val errors = beautifyListString(state.getErrors())

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
        val email = binding.email.text.toString()
        val firstName = binding.firstName.text.toString()
        val lastName = binding.lastName.text.toString()
        val password = binding.password.text.toString()

        val firstNameErrors = viewModel.validateFirstName(firstName)
        val lastNameNameErrors = viewModel.validateLastName(lastName)
        val emailErrors = viewModel.validateEmail(email)
        val passwordErrors = viewModel.validatePassword(password)

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
            viewModel.signUp(email, firstName, lastName, password)
        }
    }
}