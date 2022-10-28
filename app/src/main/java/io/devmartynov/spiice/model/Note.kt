package io.devmartynov.spiice.model

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

/**
 * Модель заметки.
 * @param id Уникальный индентификатор
 * @param title заголовок
 * @param content текст
 * @param createTime дата создания заметки в виде таймштампа.
 * @param scheduleTime дата запланированной заметки в виде таймштампа. Если дата не выбрана, то заметка незапланированная
 */
data class Note(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val content: String,
    val createTime: Long = System.currentTimeMillis(),
    val scheduleTime: Long? = null,
) {
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
