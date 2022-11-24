package io.devmartynov.spiice.repository.user

import io.devmartynov.spiice.model.user.User
import java.util.UUID

/**
 * Репозиторий пользователей
 */
interface UserRepository {
    /**
     * Получает пользователя по email
     * @param email email пользователя
     */
    suspend fun getUser(email: String): User?

    /**
     * Добавляет пользователя в базу. Если пользователь уже есть, то обновляет все его поля
     * @param user пользователь
     */
    suspend fun addUser(user: User)

    /**
     * Удаляет пользователя из базы
     */
    suspend fun deleteUser(id: UUID)
}