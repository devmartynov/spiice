package io.devmartynov.spiice.model.note

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

/**
 * Модель заметки.
 * @param id Уникальный индентификатор
 * @param title заголовок
 * @param content текст
 * @param userCreatorId id пользователя - создателя заметки
 * @param createTime дата создания заметки в виде таймштампа.
 * @param scheduleTime дата запланированной заметки в виде таймштампа. Если дата не выбрана, то заметка незапланированная
 */
@Entity
data class Note(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "userCreatorId") val userCreatorId: Long,
    @ColumnInfo(name = "createTime") val createTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "scheduleTime") val scheduleTime: Long? = null,
): Serializable {
    /**
     * Тип заметки.
     * Зависит от запланированного времени.
     */
    val noteType: NoteType
        get() {
            if (scheduleTime == null) {
                return NoteType.REGULAR
            }

            val daysDiff = java.time.Duration.between(
                Instant.ofEpochMilli(System.currentTimeMillis()),
                Instant.ofEpochMilli(scheduleTime)
            ).toDays()

            return when {
                daysDiff == 0L -> NoteType.SCHEDULED_TODAY
                daysDiff > 0L -> NoteType.SCHEDULED_FUTURE
                daysDiff < 0L -> NoteType.SCHEDULED_EXPIRED
                else -> NoteType.REGULAR
            }
        }

    companion object {
        private val dateFormat = SimpleDateFormat("dd/MM/yyyy")

        /**
         * Представляет временную метку в виде даты
         * @param time временная метка
         * @return строку даты в виде dd/MM/yyyy
         */
        fun timeAsDate(time: Long): String {
            return dateFormat.format(Date(time))
        }
    }
}
