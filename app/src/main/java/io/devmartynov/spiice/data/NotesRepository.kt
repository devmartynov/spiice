package io.devmartynov.spiice.data

import io.devmartynov.spiice.model.Note
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
     * Сохраняет заметку.
     * @param note заметка
     */
    fun saveNote(note: Note): Boolean

    /**
     * Удаляет заметку
     * @param noteId id заметки
     */
    fun deleteNote(noteId: UUID): Boolean

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
     */
    fun getNote(noteId: UUID): Note?
}