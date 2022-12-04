package io.devmartynov.spiice.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.devmartynov.spiice.R
import io.devmartynov.spiice.model.user.User
import io.devmartynov.spiice.repository.user.UserPreferencesRepository
import io.devmartynov.spiice.repository.user.UserRepository
import io.devmartynov.spiice.utils.FormAttributes
import io.devmartynov.spiice.utils.asyncOperationState.AsyncOperationState
import io.devmartynov.spiice.utils.auth.generateToken
import io.devmartynov.spiice.utils.auth.getHashedPassword
import io.devmartynov.spiice.utils.validation.ValidationResult
import io.devmartynov.spiice.validate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * VM авторизации
 * @param userRepository репозиторий пользователя
 * @param userPreferencesRepository преференцы пользователя
 * @param application конекст приложения
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    application: Application
) : AndroidViewModel(application), Auth, AuthValidation {
    private var _authState = MutableLiveData<AsyncOperationState>(AsyncOperationState.Idle)
    val authState: LiveData<AsyncOperationState> = _authState

    override fun signIn(email: String, password: String) {
        _authState.value = AsyncOperationState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.getUser(email)
            val authErrors = arrayListOf<String>()
            val app = getApplication<Application>()

            if (user == null) { // пользователя нет в базе
                authErrors.add(app.resources.getString(R.string.auth_sign_in_error))
            } else { // пользователь есть в базе
                if (!checkPassword(password, user.passwordHash)) { // ввел неверный пароль
                    authErrors.add(app.resources.getString(R.string.auth_sign_in_error))
                } else { // авторизация пользователя
                    val authToken = generateToken()
                    userRepository.addUser(user.copy(
                        token = authToken
                    ))
                    userPreferencesRepository.token = authToken
                }
            }
            _authState.postValue(
                AsyncOperationState.Success(
                    object : AuthResult {
                        override fun hasAuthErrors(): Boolean {
                            return authErrors.isNotEmpty()
                        }
                        override fun getAuthErrors(): List<String> {
                            return authErrors
                        }
                    }
                )
            )
        }
    }

    override fun signUp(email: String, firstName: String, lastName: String, password: String) {
        _authState.value = AsyncOperationState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            val user = userRepository.getUser(email)
            val authErrors = arrayListOf<String>()

            if (user == null) { // пользователя еще нет в базе
                val newUser = User(
                    email = email,
                    firstName = firstName,
                    lastName = lastName,
                    passwordHash = getHashedPassword(password),
                )
                userRepository.addUser(newUser)
                userPreferencesRepository.setUserInfo(
                    token = newUser.token,
                    email = newUser.email,
                    firstName = newUser.firstName,
                    lastName = newUser.lastName,
                    id = newUser.id
                )
            } else { // пользователь с таким email уже есть в базе
                authErrors.add(getApplication<Application>().resources.getString(R.string.auth_sign_up_error))
            }

            _authState.postValue(
                AsyncOperationState.Success(
                    object : AuthResult {
                        override fun hasAuthErrors(): Boolean {
                            return authErrors.isNotEmpty()
                        }
                        override fun getAuthErrors(): List<String> {
                            return authErrors
                        }
                    }
                )
            )
        }
    }

    override fun validateEmail(email: String): ValidationResult {
        return validate(email, FormAttributes.EMAIL)
    }

    override fun validatePassword(password: String): ValidationResult {
        return validate(password, FormAttributes.PASSWORD)
    }

    override fun validateFirstName(firstName: String): ValidationResult {
        return validate(firstName, FormAttributes.FIRST_NAME)
    }

    override fun validateLastName(lastName: String): ValidationResult {
        return validate(lastName, FormAttributes.FIRST_NAME)
    }

    private fun checkPassword(password: String, hashedPassword: String): Boolean {
        return getHashedPassword(password) == hashedPassword
    }
}