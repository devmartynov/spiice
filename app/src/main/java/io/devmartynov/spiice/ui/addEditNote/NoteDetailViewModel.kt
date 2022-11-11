package io.devmartynov.spiice.ui.addEditNote

import androidx.lifecycle.ViewModel
import io.devmartynov.spiice.repository.NotesRepository
import io.devmartynov.spiice.repository.NotesRepositoryImpl
import io.devmartynov.spiice.model.Note
import java.util.*

class NoteDetailViewModel : ViewModel() {
    private val repository: NotesRepository = NotesRepositoryImpl

    fun saveNote(note: Note): Boolean {
        return repository.saveNote(note)
    }

    fun getNote(noteId: UUID): Note? {
        return repository.getNote(noteId)
    }
}