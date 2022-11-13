package io.devmartynov.spiice.db

import androidx.room.TypeConverter
import java.util.UUID

/**
 * Конвертеры для полей модели
 */
class EntityTypeConverters {
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }
}