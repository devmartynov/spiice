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

    override fun getUser(email: String): User? {
        return userDao.getUser(email)
    }

    override fun addUser(user: User) {
        userDao.addUser(user)
    }

    override fun deleteUser(id: UUID) {
        userDao.deleteUser(id)
    }
}