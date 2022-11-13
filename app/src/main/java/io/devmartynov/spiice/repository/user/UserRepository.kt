package io.devmartynov.spiice.repository.user

import io.devmartynov.spiice.model.user.User
import java.util.UUID

/**
 * Репозиторий пользователей
 */
interface UserRepository {
    /**
     * Получает пользователя по id
     * @param id id пользователя
     */
    fun getUser(id: UUID): User?
}