package io.devmartynov.spiice.repository.note

import io.devmartynov.spiice.db.note.NoteDao
import io.devmartynov.spiice.model.note.Note
import java.util.*

/**
 * Репозиторий заметок.
 */
class NotesRepositoryImpl(private val noteDao: NoteDao) : NotesRepository {

    override suspend fun addNote(note: Note): Boolean {
        noteDao.addNote(note)
        return true
    }

    override suspend fun updateNote(note: Note): Boolean {
        noteDao.addNote(note)
        return true
    }

    override suspend fun deleteNote(note: Note): Boolean {
        noteDao.deleteNote(note)
        return true
    }

    override suspend fun getUserNotes(userId: UUID): List<Note> {
        return noteDao.getUserNotes(userId)
    }

    override suspend fun deleteAllUserNotes(userId: UUID): Boolean {
        noteDao.deleteAllUserNotes(userId)
        return true
    }

    override suspend fun getUserNotesCount(userId: UUID): Long {
        return noteDao.getUserNotesCount(userId)
    }
}