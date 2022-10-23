package io.devmartynov.spiice.view

import androidx.lifecycle.ViewModel
import io.devmartynov.spiice.data.NotesRepository
import io.devmartynov.spiice.data.NotesRepositoryImpl
import io.devmartynov.spiice.model.Note
import java.util.*

class NoteDetailViewModel() : ViewModel() {
    private val repository: NotesRepository = NotesRepositoryImpl

    fun saveNote(note: Note): Boolean {
        return repository.saveNote(note)
    }

    fun getNote(noteId: UUID): Note? {
        return repository.getNote(noteId)
    }
}