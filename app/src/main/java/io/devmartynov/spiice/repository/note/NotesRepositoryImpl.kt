package io.devmartynov.spiice.repository.note

import io.devmartynov.spiice.db.note.NoteDao
import io.devmartynov.spiice.model.note.Note
import java.util.*

/**
 * Репозиторий заметок.
 */
class NotesRepositoryImpl(private val noteDao: NoteDao) : NotesRepository {

    override fun addNote(note: Note): Boolean {
        noteDao.addNote(note)
        return true
    }

    override fun updateNote(note: Note): Boolean {
        noteDao.addNote(note)
        return true
    }

    override fun deleteNote(note: Note): Boolean {
        noteDao.deleteNote(note)
        return true
    }

    override fun getNotes(): List<Note> {
        return noteDao.getNotes()
    }

    override fun deleteAllNotes(): Boolean {
        noteDao.deleteAllNotes()
        return true
    }

    override fun getNote(noteId: UUID): Note? {
        return noteDao.getNote(noteId)
    }
}