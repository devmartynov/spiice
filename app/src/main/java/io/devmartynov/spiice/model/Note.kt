package io.devmartynov.spiice.model

import java.text.SimpleDateFormat
import java.util.*

/**
 * Модель заметки.
 * @param id Уникальный индентификатор
 * @param title заголовок
 * @param content текст
 * @param scheduleTime дата запланированной заметки в виде таймштампа. Если дата не выбрана, то заметка незапланированная
 */
data class Note(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val content: String,
    val scheduleTime: Long? = null,
) {
    val createTime: Long = System.currentTimeMillis()

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
