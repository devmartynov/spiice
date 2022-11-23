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

    /**
     * Получает пользователя по email
     * @param email email пользователя
     */
    fun getUser(email: String): User?

    /**
     * Добавляет пользователя в базу. Если пользователь уже есть, то обновляет все его поля
     * @param user пользователь
     */
    fun addUser(user: User)

    /**
     * Удаляет пользователя из базы
     */
    fun deleteUser(id: UUID)
}