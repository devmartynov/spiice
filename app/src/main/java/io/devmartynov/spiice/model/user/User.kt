package io.devmartynov.spiice.model.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

/**
 * Модель пользователя
 */
@Entity
data class User(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "email") val email: String,
    @ColumnInfo(name = "firstName") val firstName: String,
    @ColumnInfo(name = "lastName") val lastName: String,
    @ColumnInfo(name = "passwordHash") val passwordHash: String
)