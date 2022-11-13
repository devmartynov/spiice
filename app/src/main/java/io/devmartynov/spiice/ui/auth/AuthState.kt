package io.devmartynov.spiice.ui.auth

/**
 * Состояние авторизации пользователя
 */
interface AuthState {
    /**
     * Авторизован или нет
     * @return true если авторизован, иначе false
     */
    fun isAuthorized(): Boolean
}