package io.devmartynov.spiice.db.user

import androidx.room.Dao
import androidx.room.Query
import io.devmartynov.spiice.model.user.User
import java.util.UUID

/**
 * DAO для пользователей
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE id=(:id)")
    fun getUser(id: UUID): User?
}