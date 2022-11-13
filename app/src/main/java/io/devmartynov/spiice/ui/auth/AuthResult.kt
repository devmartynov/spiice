package io.devmartynov.spiice.ui.auth

/**
 * Результат авторизации
 */
interface AuthResult {
    /**
     * Есть ли ошибки авторизации?
     * @return true если есть, иначе false
     */
    fun hasAuthErrors(): Boolean

    /**
     * Ошибки авторизации
     * @return если ошибки есть, то список ошибок, иначе пустой список
     */
    fun getAuthErrors(): List<String>
}