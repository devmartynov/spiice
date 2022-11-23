package io.devmartynov.spiice.ui.auth

/**
 * Авторизаця
 */
interface Auth {
    /**
     * Вход в систему
     * @param email email пользователя
     * @param password пароль для входа
     * @return результат авторизации
     */
    fun signIn(email: String, password: String): AuthResult

    /**
     * Регистрация в системе
     * @param email email пользователя
     * @param firstName имя пользователя
     * @param lastName фамилия пользователя
     * @param password пароль для входа
     * @return результат авторизации
     */
    fun signUp(email: String, firstName: String, lastName: String, password: String): AuthResult
}