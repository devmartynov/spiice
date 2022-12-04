package io.devmartynov.spiice.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.devmartynov.spiice.db.note.NoteDao
import io.devmartynov.spiice.db.user.UserDao
import io.devmartynov.spiice.model.note.Note
import io.devmartynov.spiice.model.user.User

/**
 * Локальная база приложения
 */
@Database(
    entities = [Note::class, User::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(EntityTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun userDao(): UserDao

    companion object {
        const val DATABASE_NAME = "APP_DATABASE"
    }
}