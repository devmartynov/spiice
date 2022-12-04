package io.devmartynov.spiice.repository.user

import io.devmartynov.spiice.db.user.UserDao
import io.devmartynov.spiice.model.user.User
import java.util.*
import javax.inject.Inject

/**
 * Репозиторий пользователей.
 */
class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) : UserRepository {
    override suspend fun getUser(email: String): User? {
        return userDao.getUser(email)
    }

    override suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

    override suspend fun deleteUser(id: UUID) {
        userDao.deleteUser(id)
    }
}