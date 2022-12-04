package io.devmartynov.spiice.repository.user

import java.util.*

interface UserPreferencesRepository {
    var token: String?
    val fullName: String
    val userId: UUID

    fun setUserInfo(token: String, email: String, firstName: String, lastName: String, id: UUID)

    /**
     * Авторизован или нет
     * @return true если авторизован, иначе false
     */
    fun isAuthorized(): Boolean
}