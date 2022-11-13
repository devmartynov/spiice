package io.devmartynov.spiice.repository.user

import io.devmartynov.spiice.db.user.UserDao
import io.devmartynov.spiice.model.user.User
import java.util.*

/**
 * Репозиторий пользователей.
 */
class UserRepositoryImpl(private val userDao: UserDao) : UserRepository {
    override fun getUser(id: UUID): User? {
        return userDao.getUser(id)
    }
}