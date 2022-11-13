package io.devmartynov.spiice.repository.note

import io.devmartynov.spiice.model.note.Note
import java.util.UUID

/**
 * Репозиторий заметок
 */
interface NotesRepository {
    /**
     * Добавляет новую заметку
     * @param note новая заметка
     */
    fun addNote(note: Note): Boolean

    /**
     * Обновляет заметку
     * @param note заметка на которую будет заменена
     */
    fun updateNote(note: Note): Boolean

    /**
     * Удаляет заметку
     * @param note заметка
     */
    fun deleteNote(note: Note): Boolean

    /**
     * Список заметок
     */
    fun getNotes(): List<Note>

    /**
     * Удаляет все заметки
     */
    fun deleteAllNotes(): Boolean

    /**
     * Получает заметку по id
     * @param id id заметки
     */
    fun getNote(id: UUID): Note?
}