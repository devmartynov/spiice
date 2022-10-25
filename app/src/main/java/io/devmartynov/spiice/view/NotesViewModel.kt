package io.devmartynov.spiice.view

import androidx.lifecycle.ViewModel
import io.devmartynov.spiice.data.NotesRepository
import io.devmartynov.spiice.data.NotesRepositoryImpl
import io.devmartynov.spiice.model.Note

class NotesViewModel() : ViewModel() {
    private val repository: NotesRepository = NotesRepositoryImpl

    fun getNotes(): List<Note> {
        return repository.getNotes()
    }

    fun deleteAllNotes(): Boolean {
        return repository.deleteAllNotes()
    }
}