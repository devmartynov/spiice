package io.devmartynov.spiice.view

import androidx.lifecycle.ViewModel
import io.devmartynov.spiice.data.NotesRepository
import io.devmartynov.spiice.data.NotesRepositoryImpl
import io.devmartynov.spiice.model.Note
import java.util.UUID

class NotesViewModel: ViewModel() {
    private val repository: NotesRepository = NotesRepositoryImpl

    fun getNotes(): List<Note> {
        return repository.getNotes()
    }

    fun deleteAllNotes(): Boolean {
        return repository.deleteAllNotes()
    }

    fun deleteNote(noteId: UUID): Boolean {
        return repository.deleteNote(noteId)
    }

    fun addNote(note: Note): Boolean {
        return repository.addNote(note)
    }
}