package io.devmartynov.spiice.ui.addEditNote

import android.app.Application
import androidx.lifecycle.ViewModel
import io.devmartynov.spiice.db.AppDatabase
import io.devmartynov.spiice.repository.note.NotesRepositoryImpl
import io.devmartynov.spiice.model.note.Note
import java.util.*

/**
 * VM детальной карточки заметки
 */
class NoteDetailViewModel(application: Application) : ViewModel() {
    private val repository = NotesRepositoryImpl(
        AppDatabase.getDatabase(application).noteDao()
    )

    fun saveNote(note: Note): Boolean {
        return repository.updateNote(note)
    }

    fun getNote(noteId: UUID): Note? {
        return repository.getNote(noteId)
    }
}