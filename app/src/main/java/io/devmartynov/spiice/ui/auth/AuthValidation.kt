package io.devmartynov.spiice.ui.auth

import io.devmartynov.spiice.utils.validation.ValidationResult

/**
 * Валидация полей формы
 */
interface AuthValidation {
    /**
     * Валидирует email
     * @param email email
     * @return результат валидации
     */
    fun validateEmail(email: String): ValidationResult

    /**
     * Валидирует пароль
     * @param password пароль
     * @return результат валидации
     */
    fun validatePassword(password: String): ValidationResult

    /**
     * Валидирует имя
     * @param firstName имя
     * @return результат валидации
     */
    fun validateFirstName(firstName: String): ValidationResult

    /**
     * Валидирует имя
     * @param lastName фамилия
     * @return результат валидации
     */
    fun validateLastName(lastName: String): ValidationResult
}