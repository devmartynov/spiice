package io.devmartynov.spiice.data

import io.devmartynov.spiice.model.Note
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Репозиторий заметок.
 * Хэшмапа выбрана для удобного поиска заметки по id
 */
object NotesRepositoryImpl: NotesRepository {
    private var notes = HashMap<UUID, Note>()

    override fun addNote(note: Note): Boolean {
        if (isExisted(note.id)) {
            return false
        }
        notes[note.id] = note
        return true
    }

    override fun updateNote(note: Note): Boolean {
        if (!isExisted(note.id)) {
            return false
        }
        notes[note.id] = note
        return true
    }

    override fun saveNote(note: Note): Boolean {
        if (!addNote(note)) {
            return updateNote(note)
        }
        return true
    }

    override fun deleteNote(noteId: UUID): Boolean {
        if (!isExisted(noteId)) {
            return false
        }
        notes.remove(noteId)
        return true
    }

    override fun getNotes(): List<Note> {
        return ArrayList(notes.values)
    }

    override fun deleteAllNotes(): Boolean {
        notes.clear()
        return true
    }

    override fun getNote(noteId: UUID): Note? {
        return notes[noteId]
    }

    /**
     * Проверяет наличие заметки в репозитории по id
     * @param noteId id заметки
     * @return true если заметка есть в хранилище, иначе false
     */
    private fun isExisted(noteId: UUID): Boolean {
        return notes.containsKey(noteId)
    }
}