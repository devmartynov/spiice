package io.devmartynov.spiice.db.user

import androidx.room.*
import io.devmartynov.spiice.model.user.User
import java.util.UUID

/**
 * DAO для пользователей
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE id=(:id)")
    fun getUser(id: UUID): User?

    @Query("SELECT * FROM user WHERE email=(:email)")
    fun getUser(email: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: User)

    @Query("DELETE FROM user WHERE id=(:id)")
    fun deleteUser(id: UUID)
}