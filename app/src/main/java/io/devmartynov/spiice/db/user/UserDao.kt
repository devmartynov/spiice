package io.devmartynov.spiice.db.user

import androidx.room.*
import io.devmartynov.spiice.model.user.User
import java.util.UUID

/**
 * DAO для пользователей
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE email=(:email)")
    suspend fun getUser(email: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Query("DELETE FROM user WHERE id=(:id)")
    suspend fun deleteUser(id: UUID)
}