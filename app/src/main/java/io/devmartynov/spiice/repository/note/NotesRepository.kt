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
    suspend fun addNote(note: Note): Boolean

    /**
     * Обновляет заметку
     * @param note заметка на которую будет заменена
     */
    suspend fun updateNote(note: Note): Boolean

    /**
     * Удаляет заметку
     * @param note заметка
     */
    suspend fun deleteNote(note: Note): Boolean

    /**
     * Удаляет все заметки
     */
    suspend fun deleteAllUserNotes(userId: UUID): Boolean

    /**
     * Получает все заметки пользователя
     * @param id id пользователя
     */
    suspend fun getUserNotes(id: UUID): List<Note>

    /**
     * Получает кол-во заметок пользователя
     * @param userId id пользователя
     */
    suspend fun getUserNotesCount(userId: UUID): Long
}