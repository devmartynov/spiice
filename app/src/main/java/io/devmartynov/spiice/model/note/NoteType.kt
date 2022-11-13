package io.devmartynov.spiice.model.note

/**
 * Тип заметки.
 * Основывается на запланированном времени заметки
 */
enum class NoteType {
    REGULAR, SCHEDULED_TODAY, SCHEDULED_EXPIRED, SCHEDULED_FUTURE
}